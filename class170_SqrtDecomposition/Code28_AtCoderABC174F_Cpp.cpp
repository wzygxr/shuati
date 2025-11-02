/**
 * AtCoder ABC174F - Range Set Query - C++实现
 * 题目：区间不同元素个数查询（AtCoder版本）
 * 来源：AtCoder (https://atcoder.jp/contests/abc174/tasks/abc174_f)
 * 
 * 算法：平方根分解 + Mo's Algorithm
 * 时间复杂度：O((n+q)√n)
 * 空间复杂度：O(n)
 * 最优解：是，Mo's Algorithm是处理离线区间查询的经典最优解
 * 
 * 思路：
 * 1. 使用Mo's Algorithm处理离线查询
 * 2. 维护当前区间内不同元素的计数
 * 3. 通过移动指针动态更新计数
 * 4. 排序查询以优化指针移动效率
 * 
 * 工程化考量：
 * - 使用Mo's Algorithm优化离线查询
 * - 频率数组记录元素出现次数
 * - 排序查询以最小化指针移动
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

class RangeSetQuery {
private:
    vector<int> arr;
    vector<Query> queries;
    int block_size;
    
public:
    RangeSetQuery(vector<int>& nums, vector<pair<int, int>>& qs) {
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
    // 测试用例（AtCoder风格）
    vector<int> nums = {1, 2, 1, 3, 2, 1, 4, 5, 2, 3};
    vector<pair<int, int>> queries = {
        {0, 4},  // [1,2,1,3,2] -> 不同元素: {1,2,3} -> 3
        {1, 6},  // [2,1,3,2,1,4] -> 不同元素: {1,2,3,4} -> 4
        {2, 8},  // [1,3,2,1,4,5,2,3] -> 不同元素: {1,2,3,4,5} -> 5
        {3, 9}   // [3,2,1,4,5,2,3] -> 不同元素: {1,2,3,4,5} -> 5
    };
    
    RangeSetQuery rsq(nums, queries);
    vector<int> result = rsq.solve();
    
    cout << "Range Set Query 结果:" << endl;
    for (int i = 0; i < result.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " 
             << result[i] << " 个不同元素" << endl;
    }
    
    // 性能测试
    cout << "\n性能测试:" << endl;
    vector<int> large_nums(10000);
    for (int i = 0; i < 10000; i++) {
        large_nums[i] = rand() % 1000;
    }
    
    vector<pair<int, int>> large_queries = {
        {0, 999}, {1000, 1999}, {2000, 2999}, {3000, 3999}, {4000, 4999},
        {5000, 5999}, {6000, 6999}, {7000, 7999}, {8000, 8999}, {9000, 9999}
    };
    
    RangeSetQuery large_rsq(large_nums, large_queries);
    vector<int> large_result = large_rsq.solve();
    
    cout << "大规模测试完成，查询数量: " << large_queries.size() << endl;
    
    return 0;
}