/**
 * HDU 4638 - Group - C++实现
 * 题目：区间连续数字分组查询
 * 来源：HDU (http://acm.hdu.edu.cn/showproblem.php?pid=4638)
 * 
 * 算法：平方根分解 + Mo's Algorithm
 * 时间复杂度：O((n+q)√n)
 * 空间复杂度：O(n)
 * 最优解：是，Mo's Algorithm是处理离线区间查询的经典最优解
 * 
 * 思路：
 * 1. 使用Mo's Algorithm处理离线查询
 * 2. 维护当前区间内连续数字的分组信息
 * 3. 通过移动指针动态更新分组数量
 * 4. 利用相邻数字关系优化分组统计
 * 
 * 工程化考量：
 * - 使用Mo's Algorithm优化离线查询
 * - 维护数字出现标记数组
 * - 利用相邻关系减少分组计算
 * - 处理大规模输入输出
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
using namespace std;

struct Query {
    int l, r, idx;
};

class GroupQuery {
private:
    vector<int> arr;
    vector<Query> queries;
    int block_size;
    
public:
    GroupQuery(vector<int>& nums, vector<pair<int, int>>& qs) {
        arr = nums;
        int n = arr.size();
        block_size = sqrt(n);
        
        // 构建查询
        for (int i = 0; i < qs.size(); i++) {
            queries.push_back({qs[i].first, qs[i].second, i});
        }
    }
    
    // 比较函数：按块排序，块内按右端点排序
    static bool compare(const Query& a, const Query& b) {
        int block_a = a.l / sqrt(a.l);
        int block_b = b.l / sqrt(b.l);
        if (block_a != block_b) {
            return block_a < block_b;
        }
        return a.r < b.r;
    }
    
    vector<int> solve() {
        // 排序查询
        sort(queries.begin(), queries.end(), compare);
        
        int n = arr.size();
        int q = queries.size();
        vector<int> result(q, 0);
        
        // 标记数组，记录数字是否在当前区间
        unordered_map<int, bool> visited;
        int group_count = 0;
        
        // 初始化指针
        int cur_l = 0, cur_r = -1;
        
        for (const auto& query : queries) {
            int l = query.l;
            int r = query.r;
            
            // 移动左指针
            while (cur_l < l) {
                int num = arr[cur_l];
                visited[num] = false;
                
                // 检查相邻数字
                if (visited[num-1] && visited[num+1]) {
                    group_count++;  // 断开连接
                } else if (!visited[num-1] && !visited[num+1]) {
                    group_count--;  // 孤立数字被移除
                }
                cur_l++;
            }
            
            // 移动左指针（向左扩展）
            while (cur_l > l) {
                cur_l--;
                int num = arr[cur_l];
                visited[num] = true;
                
                // 检查相邻数字
                if (visited[num-1] && visited[num+1]) {
                    group_count--;  // 连接两个组
                } else if (!visited[num-1] && !visited[num+1]) {
                    group_count++;  // 新增孤立组
                }
            }
            
            // 移动右指针
            while (cur_r < r) {
                cur_r++;
                int num = arr[cur_r];
                visited[num] = true;
                
                // 检查相邻数字
                if (visited[num-1] && visited[num+1]) {
                    group_count--;  // 连接两个组
                } else if (!visited[num-1] && !visited[num+1]) {
                    group_count++;  // 新增孤立组
                }
            }
            
            // 移动右指针（向左收缩）
            while (cur_r > r) {
                int num = arr[cur_r];
                visited[num] = false;
                
                // 检查相邻数字
                if (visited[num-1] && visited[num+1]) {
                    group_count++;  // 断开连接
                } else if (!visited[num-1] && !visited[num+1]) {
                    group_count--;  // 移除孤立组
                }
                cur_r--;
            }
            
            result[query.idx] = group_count;
        }
        
        return result;
    }
};

int main() {
    // 测试用例
    vector<int> nums = {1, 3, 2, 4, 5, 7, 6, 8};
    vector<pair<int, int>> queries = {
        {0, 3},  // [1,3,2,4] -> 分组: {1,2,3,4} -> 1组
        {1, 5},  // [3,2,4,5,7] -> 分组: {2,3,4,5}, {7} -> 2组
        {2, 7},  // [2,4,5,7,6,8] -> 分组: {2}, {4,5}, {6,7,8} -> 3组
        {0, 7}   // 全部 -> 分组: {1,2,3,4,5}, {6,7,8} -> 2组
    };
    
    GroupQuery gq(nums, queries);
    vector<int> result = gq.solve();
    
    cout << "Group Query 结果:" << endl;
    for (int i = 0; i < result.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " 
             << result[i] << " 个分组" << endl;
    }
    
    return 0;
}