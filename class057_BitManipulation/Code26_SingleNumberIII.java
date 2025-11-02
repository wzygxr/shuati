/**
 * 只出现一次的数字 III
 * 测试链接：https://leetcode.cn/problems/single-number-iii/
 * 
 * 题目描述：
 * 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。
 * 找出只出现一次的那两个元素。你可以按任意顺序返回答案。
 * 
 * 解题思路：
 * 1. 首先对所有数字进行异或操作，得到两个不同数字的异或结果
 * 2. 找到异或结果中最低位的1，这个位置表示两个数字在该位不同
 * 3. 根据这个位将数组分成两组，分别进行异或操作得到两个结果
 * 
 * 时间复杂度：O(n) - 遍历数组两次
 * 空间复杂度：O(1) - 只使用常数个变量
 */
public class Code26_SingleNumberIII {
    
    /**
     * 找出只出现一次的两个数字
     * @param nums 整数数组
     * @return 只出现一次的两个数字
     */
    public int[] singleNumber(int[] nums) {
        if (nums == null || nums.length < 2) {
            throw new IllegalArgumentException("数组长度必须至少为2");
        }
        
        // 第一步：计算所有数字的异或结果
        int xorResult = 0;
        for (int num : nums) {
            xorResult ^= num;
        }
        
        // 第二步：找到异或结果中最低位的1
        // 技巧：xorResult & (-xorResult) 可以快速找到最低位的1
        int lowestOneBit = xorResult & (-xorResult);
        
        // 第三步：根据最低位的1将数组分成两组
        int[] result = new int[2];
        for (int num : nums) {
            // 根据该位是否为0进行分组
            if ((num & lowestOneBit) == 0) {
                result[0] ^= num;  // 第一组
            } else {
                result[1] ^= num;  // 第二组
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code26_SingleNumberIII solution = new Code26_SingleNumberIII();
        
        // 测试用例1：正常情况
        int[] nums1 = {1, 2, 1, 3, 2, 5};
        int[] result1 = solution.singleNumber(nums1);
        System.out.println("测试用例1结果: [" + result1[0] + ", " + result1[1] + "]");
        // 预期输出: [3, 5] 或 [5, 3]
        
        // 测试用例2：包含负数
        int[] nums2 = {-1, 0, -1, 2, 0, 3};
        int[] result2 = solution.singleNumber(nums2);
        System.out.println("测试用例2结果: [" + result2[0] + ", " + result2[1] + "]");
        // 预期输出: [2, 3] 或 [3, 2]
        
        // 测试用例3：边界情况
        int[] nums3 = {0, 1};
        int[] result3 = solution.singleNumber(nums3);
        System.out.println("测试用例3结果: [" + result3[0] + ", " + result3[1] + "]");
        // 预期输出: [0, 1] 或 [1, 0]
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("时间复杂度: O(n) - 遍历数组两次");
        System.out.println("空间复杂度: O(1) - 只使用常数个变量");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 输入验证：检查数组长度");
        System.out.println("2. 边界处理：处理负数情况");
        System.out.println("3. 性能优化：使用位运算替代算术运算");
        System.out.println("4. 可读性：添加详细注释说明算法原理");
    }
}