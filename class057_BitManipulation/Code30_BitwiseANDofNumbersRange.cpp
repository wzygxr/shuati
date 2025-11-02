/**
 * 数字范围按位与
 * 测试链接：https://leetcode.cn/problems/bitwise-and-of-numbers-range/
 * 
 * 题目描述：
 * 给你两个整数 left 和 right ，表示区间 [left, right] ，返回此区间内所有数字按位与的结果（包含 left 、right 端点）。
 * 
 * 解题思路：
 * 1. 暴力法：遍历区间内所有数字进行按位与操作
 * 2. 位运算技巧：找到left和right的公共前缀
 * 3. 位移法：不断右移直到left等于right，然后左移恢复
 * 4. Brian Kernighan算法：利用n & (n-1)消除最低位的1
 * 
 * 时间复杂度：O(1) - 最多32次操作
 * 空间复杂度：O(1) - 只使用常数个变量
 */
#include <iostream>
#include <climits>
using namespace std;

class Code30_BitwiseANDofNumbersRange {
public:
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(right - left)
     * 空间复杂度：O(1)
     */
    int rangeBitwiseAnd1(int left, int right) {
        int result = left;
        for (int i = left + 1; i <= right; i++) {
            result &= i;
            if (result == 0) {
                break;  // 提前终止优化
            }
        }
        return result;
    }
    
    /**
     * 方法2：位运算技巧（最优解）
     * 找到left和right的公共前缀
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    int rangeBitwiseAnd2(int left, int right) {
        int shift = 0;
        // 找到公共前缀
        while (left < right) {
            left >>= 1;
            right >>= 1;
            shift++;
        }
        return left << shift;
    }
    
    /**
     * 方法3：Brian Kernighan算法
     * 利用n & (n-1)消除最低位的1
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    int rangeBitwiseAnd3(int left, int right) {
        while (left < right) {
            // 消除right最低位的1
            right = right & (right - 1);
        }
        return right;
    }
    
    /**
     * 方法4：位移法的另一种实现
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    int rangeBitwiseAnd4(int left, int right) {
        if (left == right) {
            return left;
        }
        
        // 计算left和right的最高不同位
        int xor_val = left ^ right;
        int mask = 1 << 31;  // 从最高位开始找
        
        // 找到最高不同位
        while (mask > 0 && (xor_val & mask) == 0) {
            mask >>= 1;
        }
        
        // 创建掩码，将不同位及之后的位都置为0
        mask = (mask << 1) - 1;
        mask = ~mask;
        
        return left & mask;
    }
    
    /**
     * 方法5：利用内置函数
     * 使用GCC内置函数找到最高位
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    int rangeBitwiseAnd5(int left, int right) {
        if (left == right) return left;
        
        // 找到最高不同位
        int xor_val = left ^ right;
        int highest_bit = 31 - __builtin_clz(xor_val);
        
        // 创建掩码
        int mask = ~((1 << (highest_bit + 1)) - 1);
        return left & mask;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        Code30_BitwiseANDofNumbersRange solution;
        
        // 测试用例1：正常情况
        int left1 = 5, right1 = 7;
        int result1 = solution.rangeBitwiseAnd1(left1, right1);
        int result2 = solution.rangeBitwiseAnd2(left1, right1);
        int result3 = solution.rangeBitwiseAnd3(left1, right1);
        int result4 = solution.rangeBitwiseAnd4(left1, right1);
        int result5 = solution.rangeBitwiseAnd5(left1, right1);
        cout << "测试用例1 - 输入: [" << left1 << ", " << right1 << "]" << endl;
        cout << "方法1结果: " << result1 << " (预期: 4)" << endl;
        cout << "方法2结果: " << result2 << " (预期: 4)" << endl;
        cout << "方法3结果: " << result3 << " (预期: 4)" << endl;
        cout << "方法4结果: " << result4 << " (预期: 4)" << endl;
        cout << "方法5结果: " << result5 << " (预期: 4)" << endl;
        
        // 测试用例2：边界情况（相同数字）
        int left2 = 10, right2 = 10;
        int result6 = solution.rangeBitwiseAnd2(left2, right2);
        cout << "测试用例2 - 输入: [" << left2 << ", " << right2 << "]" << endl;
        cout << "方法2结果: " << result6 << " (预期: 10)" << endl;
        
        // 测试用例3：大范围情况
        int left3 = 1, right3 = INT_MAX;
        int result7 = solution.rangeBitwiseAnd2(left3, right3);
        cout << "测试用例3 - 输入: [" << left3 << ", " << right3 << "]" << endl;
        cout << "方法2结果: " << result7 << " (预期: 0)" << endl;
        
        // 测试用例4：包含2的幂
        int left4 = 8, right4 = 15;
        int result8 = solution.rangeBitwiseAnd2(left4, right4);
        cout << "测试用例4 - 输入: [" << left4 << ", " << right4 << "]" << endl;
        cout << "方法2结果: " << result8 << " (预期: 8)" << endl;
        
        // 复杂度分析
        cout << "\n=== 复杂度分析 ===" << endl;
        cout << "方法1 - 暴力法:" << endl;
        cout << "  时间复杂度: O(n) - n = right - left" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法2 - 位运算技巧:" << endl;
        cout << "  时间复杂度: O(1) - 最多32次位移" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法3 - Brian Kernighan算法:" << endl;
        cout << "  时间复杂度: O(1) - 最多32次操作" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法4 - 位移法变种:" << endl;
        cout << "  时间复杂度: O(1)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法5 - 内置函数法:" << endl;
        cout << "  时间复杂度: O(1)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        // 工程化考量
        cout << "\n=== 工程化考量 ===" << endl;
        cout << "1. 方法选择：" << endl;
        cout << "   - 实际工程：方法2（位运算技巧）最优" << endl;
        cout << "   - 面试场景：方法2（位运算技巧）最优" << endl;
        cout << "2. 性能优化：避免暴力法，使用位运算" << endl;
        cout << "3. 边界处理：处理left等于right的情况" << endl;
        cout << "4. 可读性：方法2代码简洁易懂" << endl;
        
        // C++特性考量
        cout << "\n=== C++特性考量 ===" << endl;
        cout << "1. 内置函数：使用GCC内置函数提高性能" << endl;
        cout << "2. 类型安全：使用int类型" << endl;
        cout << "3. 常量定义：使用INT_MAX表示最大值" << endl;
        cout << "4. 位操作：C++位操作与硬件紧密相关" << endl;
        
        // 算法技巧总结
        cout << "\n=== 算法技巧总结 ===" << endl;
        cout << "1. 公共前缀：区间按位与的结果就是公共前缀" << endl;
        cout << "2. 位移操作：通过右移找到公共前缀" << endl;
        cout << "3. Brian Kernighan技巧：消除最低位的1" << endl;
        cout << "4. 最高位掩码：使用位运算创建掩码" << endl;
        cout << "5. 提前终止：当结果为0时可以提前终止" << endl;
    }
};

int main() {
    Code30_BitwiseANDofNumbersRange::test();
    return 0;
}