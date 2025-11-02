/**
 * Codeforces 220B - Little Elephant and Array - C++实现
 * 题目：区间内出现次数等于数值的元素个数查询
 * 来源：Codeforces (https://codeforces.com/problemset/problem/220/B)
 * 
 * 算法：平方根分解 + Mo's Algorithm
 * 时间复杂度：O((n+q)√n)
 * 空间复杂度：O(n)
 * 最优解：是，Mo's Algorithm是处理离线区间查询的经典最优解
 * 
 * 思路：
 * 1. 使用Mo's Algorithm处理离线查询
 * 2. 维护当前区间内每个元素的出现次数
 * 3. 统计出现次数等于数值的元素个数
 * 4. 通过移动指针动态更新统计
 * 
 * 工程化考量：
 * - 使用Mo's Algorithm优化离线查询
 * - 频率数组记录元素出现次数
 * - 特殊处理数值大于n的情况
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

class LittleElephantArray {
private:
    vector<int> arr;
    vector<Query> queries;
    int block_size;
    
public:
    LittleElephantArray(vector<int>& nums, vector<pair<int, int>>& qs) {
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
        int valid_count = 0;
        
        // 初始化指针
        int cur_l = 0, cur_r = -1;
        
        for (const auto& query : queries) {
            int l = query.l;
            int r = query.r;
            
            // 移动左指针
            while (cur_l < l) {
                int num = arr[cur_l];
                if (num <= n) {  // 只处理数值不超过n的元素
                    if (freq[num] == num) {
                        valid_count--;
                    }
                    freq[num]--;
                    if (freq[num] == num) {
                        valid_count++;
                    }
                }
                cur_l++;
            }
            
            // 移动左指针（向左扩展）
            while (cur_l > l) {
                cur_l--;
                int num = arr[cur_l];
                if (num <= n) {  // 只处理数值不超过n的元素
                    if (freq[num] == num) {
                        valid_count--;
                    }
                    freq[num]++;
                    if (freq[num] == num) {
                        valid_count++;
                    }
                }
            }
            
            // 移动右指针
            while (cur_r < r) {
                cur_r++;
                int num = arr[cur_r];
                if (num <= n) {  // 只处理数值不超过n的元素
                    if (freq[num] == num) {
                        valid_count--;
                    }
                    freq[num]++;
                    if (freq[num] == num) {
                        valid_count++;
                    }
                }
            }
            
            // 移动右指针（向左收缩）
            while (cur_r > r) {
                int num = arr[cur_r];
                if (num <= n) {  // 只处理数值不超过n的元素
                    if (freq[num] == num) {
                        valid_count--;
                    }
                    freq[num]--;
                    if (freq[num] == num) {
                        valid_count++;
                    }
                }
                cur_r--;
            }
            
            result[query.idx] = valid_count;
        }
        
        return result;
    }
};

int main() {
    // 测试用例
    vector<int> nums = {1, 2, 3, 4, 5, 2, 3, 1, 2, 3};
    vector<pair<int, int>> queries = {
        {0, 4},  // [1,2,3,4,5] -> 1出现1次, 2出现1次 -> 有效: 1
        {1, 6},  // [2,3,4,5,2,3] -> 2出现2次, 3出现2次 -> 有效: 2
        {2, 8},  // [3,4,5,2,3,1,2,3] -> 3出现3次 -> 有效: 1
        {0, 9}   // 全部 -> 1出现2次, 2出现3次, 3出现3次 -> 有效: 1
    };
    
    LittleElephantArray lea(nums, queries);
    vector<int> result = lea.solve();
    
    cout << "Little Elephant and Array 结果:" << endl;
    for (int i = 0; i < result.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " 
             << result[i] << " 个有效元素" << endl;
    }
    
    return 0;
}