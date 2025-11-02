#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>

using namespace std;

/**
 * 上学需要的最少跳跃能力 - C++实现
 * 青蛙住在一条河边，家在0位置, 每天到河对岸的上学，学校在n位置
 * 河里的石头排成了一条直线，青蛙每次跳跃必须落在一块石头或者岸上
 * 给定一个长度为n-1的数组arr，表示1~n-1位置每块石头的高度数值
 * 每次青蛙从一块石头起跳，这块石头的高度就会下降1
 * 当石头的高度下降到0时，青蛙不能再跳到这块石头上，跳跃后使石头高度下降到0是允许的
 * 青蛙一共需要去学校上x天课, 所以它需要往返x次，青蛙具有跳跃能力y, 它可以跳跃不超过y的距离
 * 请问青蛙的跳跃能力至少是多少，才能用这些石头往返x次
 * 1 <= n <= 10^5
 * 1 <= arr[i] <= 10^4
 * 1 <= x <= 10^9
 * 测试链接 : https://www.luogu.com.cn/problem/P8775
 * 
 * 算法思路：
 * 1. 二分答案：将问题转化为判定问题，对于每个可能的跳跃能力y，验证是否能完成x次往返
 * 2. 滑动窗口：对于给定的y，使用滑动窗口验证是否可行
 * 3. 模拟往返：每次往返消耗窗口内石头的高度
 * 
 * 时间复杂度：O(n * log(n))，其中n是石头的数量
 * 空间复杂度：O(n)，需要复制数组进行验证
 */

class FrogToSchool {
public:
    int compute(int n, int x, vector<int>& arr) {
        // 将学校位置n的高度设为足够大
        arr.resize(n + 1);
        arr[n] = 2 * x;
        
        int left = 1;
        int right = n;
        int ans = 0;
        
        // 二分查找最小跳跃能力
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canFinish(n, x, arr, mid)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
    
private:
    /**
     * 检查给定跳跃能力是否能完成x次往返
     * @param n 学校位置
     * @param x 往返次数
     * @param arr 石头高度数组
     * @param power 跳跃能力
     * @return 是否能完成x次往返
     */
    bool canFinish(int n, int x, vector<int> arr, int power) {
        // 复制数组，避免修改原数组
        vector<int> temp = arr;
        
        // 模拟x次往返
        for (int i = 0; i < x; i++) {
            long long sum = 0;
            int l = 1;
            int r = 1;
            
            // 滑动窗口验证
            while (l <= n && r <= n) {
                // 扩展右边界
                while (r <= n && r - l < power) {
                    sum += temp[r];
                    r++;
                }
                
                // 检查窗口内石头是否足够
                if (sum >= 2) {
                    long long need = min(sum, 2LL);
                    sum -= need;
                    
                    // 消耗石头高度
                    for (int j = l; j < r && need > 0; j++) {
                        long long deduct = min((long long)temp[j], need);
                        temp[j] -= deduct;
                        need -= deduct;
                    }
                    
                    if (need == 0) {
                        break;
                    }
                }
                
                // 移动左边界
                sum -= temp[l];
                l++;
            }
            
            // 如果无法完成本次往返
            if (sum < 2) {
                return false;
            }
        }
        
        return true;
    }
};

/**
 * 优化版本：使用数学分析优化验证过程
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
class FrogToSchoolOptimized {
public:
    int compute(int n, int x, vector<int>& arr) {
        // 将学校位置n的高度设为足够大
        arr.resize(n + 1);
        arr[n] = 2 * x;
        
        // 计算每个位置最多能被使用的次数
        vector<long long> usage(n + 1);
        for (int i = 1; i <= n; i++) {
            usage[i] = min((long long)arr[i], (long long)x);
        }
        
        int left = 1;
        int right = n;
        int ans = 0;
        
        // 二分查找
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canFinishOptimized(n, x, usage, mid)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
    
private:
    bool canFinishOptimized(int n, int x, vector<long long>& usage, int power) {
        long long total = 0;
        int l = 1;
        
        // 滑动窗口计算总可用次数
        for (int r = 1; r <= n; r++) {
            total += usage[r];
            
            // 维护窗口大小不超过power
            while (r - l + 1 > power) {
                total -= usage[l];
                l++;
            }
            
            // 如果窗口大小达到power且总可用次数不足2x
            if (r - l + 1 == power && total < 2LL * x) {
                return false;
            }
        }
        
        return true;
    }
};

/**
 * 测试函数
 */
int main() {
    FrogToSchool solution;
    
    // 测试用例1
    int n1 = 5;
    int x1 = 3;
    vector<int> arr1 = {0, 2, 3, 1, 4}; // 注意：数组从位置1开始
    cout << "Test 1: " << solution.compute(n1, x1, arr1) << endl;
    
    // 测试用例2
    int n2 = 10;
    int x2 = 5;
    vector<int> arr2 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    cout << "Test 2: " << solution.compute(n2, x2, arr2) << endl;
    
    // 优化版本测试
    FrogToSchoolOptimized solutionOpt;
    cout << "Optimized Test 1: " << solutionOpt.compute(n1, x1, arr1) << endl;
    cout << "Optimized Test 2: " << solutionOpt.compute(n2, x2, arr2) << endl;
    
    return 0;
}

/**
 * C++工程化实战建议：
 * 
 * 1. 内存管理：
 *    - 使用vector代替原生数组，避免手动内存管理
 *    - 注意vector的resize操作，确保索引不越界
 *    - 对于大规模数据，考虑使用reserve预分配内存
 * 
 * 2. 类型安全：
 *    - 使用size_t处理数组索引，避免负数问题
 *    - 对于大数运算，使用long long避免溢出
 *    - 注意整数类型转换，避免精度丢失
 * 
 * 3. 性能优化：
 *    - 使用引用传递避免不必要的拷贝
 *    - 对于频繁调用的函数，考虑内联优化
 *    - 使用-O2或-O3编译优化
 * 
 * 4. 输入输出优化：
 *    - 使用ios::sync_with_stdio(false)加速cin/cout
 *    - 对于大规模输入，考虑使用scanf/printf
 *    - 使用endl会刷新缓冲区，在性能敏感场景使用'\n'
 * 
 * 5. 异常处理：
 *    - 添加输入合法性检查
 *    - 使用try-catch处理可能的异常
 *    - 确保资源正确释放
 * 
 * 6. 调试技巧：
 *    - 使用gdb进行调试
 *    - 添加assert断言验证中间结果
 *    - 使用valgrind检查内存泄漏
 */