/**
 * Codeforces 86D. Powerful array - C++实现
 * 题目：区间元素出现次数的平方和查询
 * 来源：Codeforces (https://codeforces.com/problemset/problem/86/D)
 * 
 * 算法：平方根分解 + Mo's Algorithm
 * 时间复杂度：O((n+q)√n)
 * 空间复杂度：O(n)
 * 最优解：是，Mo's Algorithm是处理离线区间查询的经典最优解
 * 
 * 思路：
 * 1. 使用Mo's Algorithm处理离线查询
 * 2. 维护当前区间内每个元素的出现次数
 * 3. 计算∑(count[x]² * x)作为查询结果
 * 4. 通过移动指针动态更新平方和
 * 
 * 工程化考量：
 * - 使用Mo's Algorithm优化离线查询
 * - 动态维护平方和，避免重复计算
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

class PowerfulArray {
private:
    vector<int> arr;
    vector<Query> queries;
    int block_size;
    
public:
    PowerfulArray(vector<int>& nums, vector<pair<int, int>>& qs) {
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
    
    vector<long long> solve() {
        // 排序查询
        sort(queries.begin(), queries.end(), compare);
        
        int n = arr.size();
        int q = queries.size();
        vector<long long> result(q, 0);
        
        // 频率数组
        unordered_map<int, int> freq;
        long long current_sum = 0;
        
        // 初始化指针
        int cur_l = 0, cur_r = -1;
        
        for (const auto& query : queries) {
            int l = query.l;
            int r = query.r;
            
            // 移动左指针
            while (cur_l < l) {
                int x = arr[cur_l];
                current_sum -= (long long)freq[x] * freq[x] * x;
                freq[x]--;
                current_sum += (long long)freq[x] * freq[x] * x;
                cur_l++;
            }
            
            // 移动左指针（向左扩展）
            while (cur_l > l) {
                cur_l--;
                int x = arr[cur_l];
                current_sum -= (long long)freq[x] * freq[x] * x;
                freq[x]++;
                current_sum += (long long)freq[x] * freq[x] * x;
            }
            
            // 移动右指针
            while (cur_r < r) {
                cur_r++;
                int x = arr[cur_r];
                current_sum -= (long long)freq[x] * freq[x] * x;
                freq[x]++;
                current_sum += (long long)freq[x] * freq[x] * x;
            }
            
            // 移动右指针（向左收缩）
            while (cur_r > r) {
                int x = arr[cur_r];
                current_sum -= (long long)freq[x] * freq[x] * x;
                freq[x]--;
                current_sum += (long long)freq[x] * freq[x] * x;
                cur_r--;
            }
            
            result[query.idx] = current_sum;
        }
        
        return result;
    }
};

int main() {
    // 测试用例
    vector<int> nums = {1, 2, 1, 3, 2, 1, 4};
    vector<pair<int, int>> queries = {
        {0, 3},  // [1,2,1,3] -> 1²*1 + 2²*2 + 1²*1 + 1²*3 = 1 + 8 + 1 + 3 = 13
        {1, 5},  // [2,1,3,2,1] -> 2²*2 + 2²*1 + 1²*3 + 2²*2 + 2²*1 = 8 + 4 + 3 + 8 + 4 = 27
        {2, 6}   // [1,3,2,1,4] -> 2²*1 + 1²*3 + 1²*2 + 2²*1 + 1²*4 = 4 + 3 + 2 + 4 + 4 = 17
    };
    
    PowerfulArray pa(nums, queries);
    vector<long long> result = pa.solve();
    
    cout << "Powerful Array 结果:" << endl;
    for (int i = 0; i < result.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " 
             << result[i] << endl;
    }
    
    return 0;
}