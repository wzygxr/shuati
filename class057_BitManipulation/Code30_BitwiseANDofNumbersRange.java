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
public class Code30_BitwiseANDofNumbersRange {
    
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(right - left)
     * 空间复杂度：O(1)
     */
    public int rangeBitwiseAnd1(int left, int right) {
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
    public int rangeBitwiseAnd2(int left, int right) {
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
    public int rangeBitwiseAnd3(int left, int right) {
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
    public int rangeBitwiseAnd4(int left, int right) {
        if (left == right) {
            return left;
        }
        
        // 计算left和right的最高不同位
        int xor = left ^ right;
        int mask = Integer.highestOneBit(xor);
        
        // 创建掩码，将不同位及之后的位都置为0
        mask = (mask << 1) - 1;
        mask = ~mask;
        
        return left & mask;
    }
    
    /**
     * 方法5：数学方法
     * 利用2的幂的性质
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public int rangeBitwiseAnd5(int left, int right) {
        // 如果区间跨越了2的幂的边界，结果为0
        if ((left & (left - 1)) > 0 || (right & (right - 1)) > 0) {
            int nextPower = Integer.highestOneBit(left) << 1;
            if (right >= nextPower) {
                return 0;
            }
        }
        
        int result = left;
        for (int i = left + 1; i <= right; i++) {
            result &= i;
            if (result == 0) break;
        }
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code30_BitwiseANDofNumbersRange solution = new Code30_BitwiseANDofNumbersRange();
        
        // 测试用例1：正常情况
        int left1 = 5, right1 = 7;
        int result1 = solution.rangeBitwiseAnd1(left1, right1);
        int result2 = solution.rangeBitwiseAnd2(left1, right1);
        int result3 = solution.rangeBitwiseAnd3(left1, right1);
        int result4 = solution.rangeBitwiseAnd4(left1, right1);
        int result5 = solution.rangeBitwiseAnd5(left1, right1);
        System.out.println("测试用例1 - 输入: [" + left1 + ", " + right1 + "]");
        System.out.println("方法1结果: " + result1 + " (预期: 4)");
        System.out.println("方法2结果: " + result2 + " (预期: 4)");
        System.out.println("方法3结果: " + result3 + " (预期: 4)");
        System.out.println("方法4结果: " + result4 + " (预期: 4)");
        System.out.println("方法5结果: " + result5 + " (预期: 4)");
        
        // 测试用例2：边界情况（相同数字）
        int left2 = 10, right2 = 10;
        int result6 = solution.rangeBitwiseAnd2(left2, right2);
        System.out.println("测试用例2 - 输入: [" + left2 + ", " + right2 + "]");
        System.out.println("方法2结果: " + result6 + " (预期: 10)");
        
        // 测试用例3：大范围情况
        int left3 = 1, right3 = 2147483647;
        int result7 = solution.rangeBitwiseAnd2(left3, right3);
        System.out.println("测试用例3 - 输入: [" + left3 + ", " + right3 + "]");
        System.out.println("方法2结果: " + result7 + " (预期: 0)");
        
        // 测试用例4：包含2的幂
        int left4 = 8, right4 = 15;
        int result8 = solution.rangeBitwiseAnd2(left4, right4);
        System.out.println("测试用例4 - 输入: [" + left4 + ", " + right4 + "]");
        System.out.println("方法2结果: " + result8 + " (预期: 8)");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 暴力法:");
        System.out.println("  时间复杂度: O(n) - n = right - left");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - 位运算技巧:");
        System.out.println("  时间复杂度: O(1) - 最多32次位移");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法3 - Brian Kernighan算法:");
        System.out.println("  时间复杂度: O(1) - 最多32次操作");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法4 - 位移法变种:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法5 - 数学方法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 方法选择：");
        System.out.println("   - 实际工程：方法2（位运算技巧）最优");
        System.out.println("   - 面试场景：方法2（位运算技巧）最优");
        System.out.println("2. 性能优化：避免暴力法，使用位运算");
        System.out.println("3. 边界处理：处理left等于right的情况");
        System.out.println("4. 可读性：方法2代码简洁易懂");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 公共前缀：区间按位与的结果就是公共前缀");
        System.out.println("2. 位移操作：通过右移找到公共前缀");
        System.out.println("3. Brian Kernighan技巧：消除最低位的1");
        System.out.println("4. 最高位掩码：使用Integer.highestOneBit");
        System.out.println("5. 提前终止：当结果为0时可以提前终止");
        
        // 应用场景
        System.out.println("\n=== 应用场景 ===");
        System.out.println("1. IP地址范围计算");
        System.out.println("2. 权限范围检查");
        System.out.println("3. 位图操作中的范围查询");
        System.out.println("4. 网络编程中的掩码计算");
    }
}