#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;

/**
 * 差分数组实现 (C++版本)
 * 
 * 算法思路：
 * 差分数组是一种用于高效处理区间更新操作的数据结构。
 * 通过维护原数组的差分数组，可以将区间更新操作的时间复杂度从O(n)降低到O(1)。
 * 
 * 应用场景：
 * 1. 数组操作优化：批量更新处理
 * 2. 前缀和计算：快速计算区间和
 * 3. 算法竞赛：区间操作问题的优化
 * 
 * 时间复杂度：
 * - 区间更新：O(1)
 * - 获取结果数组：O(n)
 * - 单点查询：O(n)（需要重建数组）
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode 370. 区间加法
 * 2. LeetCode 1094. 拼车
 * 3. LeetCode 1109. 航班预订统计
 */

class DifferenceArray {
private:
    vector<int> diff;     // 差分数组
    int size;             // 数组大小
    vector<int> original; // 原始数组（用于重置操作）
    
public:
    /**
     * 构造函数 - 从大小创建
     * @param size 数组大小
     */
    DifferenceArray(int size) {
        if (size <= 0) {
            throw invalid_argument("数组大小必须为正整数");
        }
        
        this->size = size;
        this->diff.assign(size + 1, 0);  // 差分数组大小为n+1，便于处理边界
        this->original.assign(size, 0);
    }
    
    /**
     * 构造函数 - 从原始数组创建
     * @param originalArray 原始数组
     */
    DifferenceArray(const vector<int>& originalArray) {
        if (originalArray.empty()) {
            throw invalid_argument("输入数组不能为空");
        }
        
        this->size = originalArray.size();
        this->original = originalArray;
        this->diff.assign(size + 1, 0);
        
        // 初始化差分数组
        diff[0] = originalArray[0];
        for (int i = 1; i < size; i++) {
            diff[i] = originalArray[i] - originalArray[i - 1];
        }
    }
    
    /**
     * 区间更新：将区间[start, end]的每个元素加上val
     * 时间复杂度：O(1)
     * @param start 起始索引（包含）
     * @param end 结束索引（包含）
     * @param val 要增加的值
     */
    void rangeUpdate(int start, int end, int val) {
        if (start < 0 || end >= size || start > end) {
            throw invalid_argument("更新范围无效");
        }
        
        diff[start] += val;
        diff[end + 1] -= val;
    }
    
    /**
     * 获取更新后的数组
     * 时间复杂度：O(n)
     * @return 更新后的数组
     */
    vector<int> getResult() {
        vector<int> result(size);
        result[0] = diff[0];
        
        for (int i = 1; i < size; i++) {
            result[i] = result[i - 1] + diff[i];
        }
        
        return result;
    }
    
    /**
     * 直接获取数组中特定位置的值
     * 注意：这需要先重建数组，时间复杂度O(n)
     * @param index 索引位置
     * @return 该位置的值
     */
    int getValue(int index) {
        if (index < 0 || index >= size) {
            throw invalid_argument("索引无效");
        }
        
        vector<int> result = getResult();
        return result[index];
    }
    
    /**
     * 重置差分数组
     */
    void reset() {
        fill(diff.begin(), diff.end(), 0);
        if (!original.empty()) {
            diff[0] = original[0];
            for (int i = 1; i < size; i++) {
                diff[i] = original[i] - original[i - 1];
            }
        }
    }
    
    /**
     * 获取数组大小
     * @return 数组大小
     */
    int getSize() const {
        return size;
    }
};

/**
 * 测试差分数组
 */
void testDifferenceArray() {
    cout << "=== 测试差分数组 ===" << endl;
    
    // 测试从大小创建
    cout << "测试从大小创建:" << endl;
    DifferenceArray da1(5);
    vector<int> result1 = da1.getResult();
    cout << "初始数组: ";
    for (int val : result1) cout << val << " ";
    cout << endl;
    
    da1.rangeUpdate(0, 2, 1);
    vector<int> result2 = da1.getResult();
    cout << "区间[0,2]加1: ";
    for (int val : result2) cout << val << " ";
    cout << endl;
    
    da1.rangeUpdate(1, 4, 2);
    vector<int> result3 = da1.getResult();
    cout << "区间[1,4]加2: ";
    for (int val : result3) cout << val << " ";
    cout << endl;
    
    da1.rangeUpdate(2, 3, -1);
    vector<int> result4 = da1.getResult();
    cout << "区间[2,3]减1: ";
    for (int val : result4) cout << val << " ";
    cout << endl;
    
    // 测试从原始数组创建
    cout << "\n测试从原始数组创建:" << endl;
    vector<int> original = {1, 2, 3, 4, 5};
    DifferenceArray da2(original);
    vector<int> result5 = da2.getResult();
    cout << "原始数组: ";
    for (int val : result5) cout << val << " ";
    cout << endl;
    
    da2.rangeUpdate(1, 3, 10);
    vector<int> result6 = da2.getResult();
    cout << "区间[1,3]加10: ";
    for (int val : result6) cout << val << " ";
    cout << endl;
    
    da2.rangeUpdate(0, 4, -5);
    vector<int> result7 = da2.getResult();
    cout << "区间[0,4]减5: ";
    for (int val : result7) cout << val << " ";
    cout << endl;
    
    // 测试重置功能
    da2.reset();
    vector<int> result8 = da2.getResult();
    cout << "重置后: ";
    for (int val : result8) cout << val << " ";
    cout << endl;
    
    // 测试边界情况
    cout << "\n测试边界情况:" << endl;
    DifferenceArray da3(1);
    da3.rangeUpdate(0, 0, 100);
    vector<int> result9 = da3.getResult();
    cout << "单元素数组更新: ";
    for (int val : result9) cout << val << " ";
    cout << endl;
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    
    // 测试大量区间更新操作
    int n = 100000;
    DifferenceArray da4(n);
    
    auto startTime = chrono::high_resolution_clock::now();
    for (int i = 0; i < 10000; i++) {
        int start = i % (n - 100);
        int end = min(start + 100, n - 1);
        da4.rangeUpdate(start, end, 1);
    }
    auto endTime = chrono::high_resolution_clock::now();
    
    auto updateTime = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    // 获取结果数组
    startTime = chrono::high_resolution_clock::now();
    vector<int> result = da4.getResult();
    endTime = chrono::high_resolution_clock::now();
    
    auto getResultTime = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "执行10000次区间更新时间: " << updateTime.count() / 1000.0 << " ms" << endl;
    cout << "获取100000元素结果数组时间: " << getResultTime.count() / 1000.0 << " ms" << endl;
    cout << "结果数组前10个元素: ";
    for (int i = 0; i < 10; i++) {
        cout << result[i] << " ";
    }
    cout << endl;
}

int main() {
    testDifferenceArray();
    return 0;
}