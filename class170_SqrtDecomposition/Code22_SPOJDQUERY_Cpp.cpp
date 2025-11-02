/**
 * SPOJ DQUERY - D-query - C++实现
 * 题目：区间不同元素个数查询
 * 来源：SPOJ (https://www.spoj.com/problems/DQUERY/)
 * 
 * 算法：平方根分解 + Mo's Algorithm
 * 时间复杂度：O((n+q)√n)
 * 空间复杂度：O(n)
 * 最优解：是，Mo's Algorithm是处理离线区间查询的经典最优解
 * 
 * 思路：
 * 1. 使用Mo's Algorithm处理离线查询
 * 2. 将查询按块排序，块内按右端点排序
 * 3. 维护当前区间内不同元素的计数
 * 4. 通过移动左右指针来更新计数
 * 
 * 工程化考量：
 * - 使用Mo's Algorithm优化离线查询
 * - 块大小选择√n，平衡查询效率
 * - 使用频率数组记录元素出现次数
 * - 排序查询以优化指针移动
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

class DQuery {
private:
    vector<int> arr;
    vector<Query> queries;
    int block_size;
    
public:
    DQuery(vector<int>& nums, vector<pair<int, int>>& qs) {
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
        
        // 频率数组
        unordered_map<int, int> freq;
        int distinct_count = 0;
        
        // 初始化指针
        int cur_l = 0, cur_r = -1;
        
        for (const auto& query : queries) {
            int l = query.l;
            int r = query.r;
            
            // 移动左指针
            while (cur_l < l) {
                freq[arr[cur_l]]--;
                if (freq[arr[cur_l]] == 0) {
                    distinct_count--;
                }
                cur_l++;
            }
            
            // 移动左指针（向左扩展）
            while (cur_l > l) {
                cur_l--;
                freq[arr[cur_l]]++;
                if (freq[arr[cur_l]] == 1) {
                    distinct_count++;
                }
            }
            
            // 移动右指针
            while (cur_r < r) {
                cur_r++;
                freq[arr[cur_r]]++;
                if (freq[arr[cur_r]] == 1) {
                    distinct_count++;
                }
            }
            
            // 移动右指针（向左收缩）
            while (cur_r > r) {
                freq[arr[cur_r]]--;
                if (freq[arr[cur_r]] == 0) {
                    distinct_count--;
                }
                cur_r--;
            }
            
            result[query.idx] = distinct_count;
        }
        
        return result;
    }
};

int main() {
    // 测试用例
    vector<int> nums = {1, 1, 2, 1, 3, 2, 3, 4, 1};
    vector<pair<int, int>> queries = {
        {0, 4},  // [1,1,2,1,3] -> 不同元素: {1,2,3} -> 3
        {1, 3},  // [1,2,1] -> 不同元素: {1,2} -> 2
        {2, 6},  // [2,1,3,2,3] -> 不同元素: {1,2,3} -> 3
        {0, 8}   // 全部元素 -> 不同元素: {1,2,3,4} -> 4
    };
    
    DQuery dq(nums, queries);
    vector<int> result = dq.solve();
    
    cout << "D-query 结果:" << endl;
    for (int i = 0; i < result.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " 
             << result[i] << " 个不同元素" << endl;
    }
    
    return 0;
}