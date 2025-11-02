package class031;

// 数字范围按位与
// 测试链接 : https://leetcode.cn/problems/bitwise-and-of-numbers-range/
/*
题目描述：
给你两个整数 left 和 right ，表示区间 [left, right] ，返回此区间内所有数字按位与的结果（包含 left 、right 端点）。

示例：
输入：left = 5, right = 7
输出：4

输入：left = 0, right = 0
输出：0

输入：left = 1, right = 2147483647
输出：0

提示：
0 <= left <= right <= 2^31 - 1

解题思路：
这道题要求计算区间[left, right]内所有数字按位与的结果。

方法1：暴力解法
遍历区间内所有数字，逐一进行按位与运算。
时间复杂度：O(right - left)
空间复杂度：O(1)
当区间较大时会超时。

方法2：找公共前缀（推荐）
核心思想：区间内所有数字按位与的结果就是left和right的二进制表示的公共前缀。

为什么是公共前缀？
考虑区间[5, 7]：
5: 101
6: 110
7: 111

从右到左逐位分析：
- 最低位：5(1), 6(0), 7(1) -> 包含0和1，按位与结果为0
- 第二位：5(0), 6(1), 7(1) -> 包含0和1，按位与结果为0
- 第三位：5(1), 6(1), 7(1) -> 全为1，按位与结果为1

所以结果为100(二进制) = 4(十进制)

更进一步观察发现，结果就是left和right的公共前缀。

实现方法：
1. 不断右移left和right，直到它们相等（找到公共前缀）
2. 记录右移的位数
3. 将公共前缀左移相应的位数

例如：left=5, right=7
5: 101
7: 111

右移过程：
第1次：left=10, right=11, shift=1
第2次：left=1, right=1, shift=2

公共前缀为1，左移2位得到100(二进制)=4(十进制)

方法3：Brian Kernighan算法
核心思想：不断移除right最右边的1，直到left >= right。

时间复杂度：
方法1：O(right - left)
方法2：O(log n)
方法3：O(log n)

空间复杂度：O(1)
*/
public class Code15_BitwiseANDofNumbersRange {

    /**
     * 计算区间[left, right]内所有数字按位与的结果
     * @param left 区间左端点
     * @param right 区间右端点
     * @return 区间内所有数字按位与的结果
     */
    public static int rangeBitwiseAnd(int left, int right) {
        // 方法2：找公共前缀
        int shift = 0;
        // 找到left和right的公共前缀
        while (left != right) {
            left >>= 1;
            right >>= 1;
            shift++;
        }
        return left << shift;
    }
    
    // 方法3：Brian Kernighan算法
    // public static int rangeBitwiseAnd(int left, int right) {
    //     // 不断移除right最右边的1，直到left >= right
    //     while (left < right) {
    //         // right & -right 可以提取出right最右边的1
    //         // right -= right & -right 相当于移除了最右边的1
    //         right -= right & -right;
    //     }
    //     return right;
    // }
    
    // 方法1：暴力解法（仅适用于小区间）
    // public static int rangeBitwiseAnd(int left, int right) {
    //     int result = left;
    //     for (int i = left + 1; i <= right; i++) {
    //         result &= i;
    //         // 优化：如果结果已经为0，可以提前结束
    //         if (result == 0) {
    //             break;
    //         }
    //     }
    //     return result;
    // }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("Test 1: " + rangeBitwiseAnd(5, 7));           // 输出: 4
        System.out.println("Test 2: " + rangeBitwiseAnd(0, 0));           // 输出: 0
        System.out.println("Test 3: " + rangeBitwiseAnd(1, 2147483647));  // 输出: 0
        System.out.println("Test 4: " + rangeBitwiseAnd(26, 30));         // 输出: 24
    }

}