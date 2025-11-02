package class030;

/**
 * 异或运算的性质和技巧
 * 
 * 本文件总结了异或运算的重要性质和常用技巧，并通过实例展示其应用
 */
public class Code09_XorPropertiesAndTricks {

    /**
     * 异或运算的基本性质
     * 
     * 1. 归零律: a ^ a = 0
     *    任何数与自身异或结果为0
     *    
     * 2. 恒等律: a ^ 0 = a
     *    任何数与0异或结果为其本身
     *    
     * 3. 交换律: a ^ b = b ^ a
     *    异或运算满足交换律
     *    
     * 4. 结合律: (a ^ b) ^ c = a ^ (b ^ c)
     *    异或运算满足结合律
     *    
     * 5. 自反性: a ^ b ^ a = b
     *    一个数与另一个数异或两次等于自身
     */

    /**
     * 技巧1: 交换两个数的值（不使用额外变量）
     * 
     * 原理: 利用自反性 a ^ b ^ a = b
     * 
     * 步骤:
     * 1. a = a ^ b
     * 2. b = a ^ b (实际上是原a ^ b ^ 原b = 原a)
     * 3. a = a ^ b (实际上是原a ^ b ^ 原a = 原b)
     */
    public static void swap(int[] arr, int i, int j) {
        if (i != j) { // 防止相同索引导致变为0
            arr[i] = arr[i] ^ arr[j];
            arr[j] = arr[i] ^ arr[j];
            arr[i] = arr[i] ^ arr[j];
        }
    }

    /**
     * 技巧2: 找到数组中唯一出现奇数次的元素
     * 
     * 原理: 利用归零律和恒等律
     * 出现偶数次的元素异或后变为0，出现奇数次的元素异或后保留本身
     */
    public static int findOddOccurrence(int[] arr) {
        int result = 0;
        for (int num : arr) {
            result ^= num;
        }
        return result;
    }

    /**
     * 技巧3: 找到数组中两个唯一出现奇数次的元素
     * 
     * 原理:
     * 1. 对所有元素异或得到 a ^ b
     * 2. 找到 a ^ b 中最右边的1位，根据该位将数组分为两组
     * 3. 两个目标数分别在两组中，对每组分别异或得到两个数
     */
    public static int[] findTwoOddOccurrences(int[] arr) {
        // 1. 对所有数字异或，得到 a ^ b
        int xor = 0;
        for (int num : arr) {
            xor ^= num;
        }

        // 2. 找到最右边的1位 (Brian Kernighan算法)
        int rightmostBit = xor & (-xor);

        // 3. 根据该位是否为1将数组分为两组
        int xor1 = 0, xor2 = 0;
        for (int num : arr) {
            if ((num & rightmostBit) == 0) {
                xor1 ^= num;
            } else {
                xor2 ^= num;
            }
        }

        return new int[]{xor1, xor2};
    }

    /**
     * 技巧4: 判断一个数是否为2的幂
     * 
     * 原理: 2的幂的二进制表示只有一个1，如 1000
     *      该数减1后变成 0111
     *      两者相与结果为0
     */
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * 技巧5: 找到最右边的1位
     * 
     * 原理: Brian Kernighan算法
     *      n & (-n) 可以提取出最右边的1位
     */
    public static int getRightmostBit(int n) {
        return n & (-n);
    }

    /**
     * 技巧6: 清除最右边的1位
     * 
     * 原理: n & (n-1) 可以清除最右边的1位
     */
    public static int clearRightmostBit(int n) {
        return n & (n - 1);
    }

    /**
     * 技巧7: 计算一个数二进制表示中1的个数
     * 
     * 原理: 利用清除最右边1位的方法
     */
    public static int countOnes(int n) {
        int count = 0;
        while (n != 0) {
            count++;
            n = clearRightmostBit(n);
        }
        return count;
    }

    /**
     * 技巧8: 构造掩码
     * 
     * 构造一个指定位数且所有位都为1的掩码
     */
    public static int createMask(int bits) {
        return (1 << bits) - 1;
    }

    /**
     * 技巧9: 获取/设置/清除/翻转指定位
     */
    
    // 获取第i位的值 (从0开始)
    public static int getBit(int num, int i) {
        return (num >> i) & 1;
    }
    
    // 设置第i位为1
    public static int setBit(int num, int i) {
        return num | (1 << i);
    }
    
    // 清除第i位(变为0)
    public static int clearBit(int num, int i) {
        return num & ~(1 << i);
    }
    
    // 翻转第i位
    public static int flipBit(int num, int i) {
        return num ^ (1 << i);
    }

    /**
     * 实际应用示例
     */

    /**
     * 示例1: 寻找缺失的数字
     * 
     * 问题: 在数组[0,1,2,...,n]中缺少了一个数字，找出这个数字
     * 方法: 利用异或的性质，将索引和数组元素一起异或
     */
    public static int findMissingNumber(int[] nums) {
        int n = nums.length;
        int result = n; // 初始值设为n，因为循环中不会处理到索引n
        
        for (int i = 0; i < n; i++) {
            result ^= i ^ nums[i];
        }
        
        return result;
    }

    /**
     * 示例2: 计算汉明距离总和
     * 
     * 问题: 计算数组中任意两个数汉明距离的总和
     * 方法: 按位统计，对每一位计算0和1的组合数
     */
    public static int totalHammingDistance(int[] nums) {
        int total = 0;
        int n = nums.length;
        
        // 对每一位进行统计
        for (int i = 0; i < 32; i++) {
            int countOnes = 0;
            // 统计第i位上1的个数
            for (int num : nums) {
                countOnes += (num >> i) & 1;
            }
            // 第i位上汉明距离总和 = 1的个数 * 0的个数
            total += countOnes * (n - countOnes);
        }
        
        return total;
    }

    /**
     * 示例3: 找到数组中重复和缺失的数字
     * 
     * 问题: 数组包含从1到n的整数，但其中一个数字重复，另一个数字缺失
     * 方法: 利用异或和数学公式
     */
    public static int[] findErrorNums(int[] nums) {
        int n = nums.length;
        int xor = 0;
        
        // 1. 计算 nums[0] ^ nums[1] ^ ... ^ nums[n-1] ^ 1 ^ 2 ^ ... ^ n
        // 结果为 重复的数 ^ 缺失的数
        for (int i = 0; i < n; i++) {
            xor ^= nums[i] ^ (i + 1);
        }
        
        // 2. 找到最右边的1位，将数字分为两组
        int rightmostBit = xor & (-xor);
        
        int xor1 = 0, xor2 = 0;
        // 3. 分别异或两组数字
        for (int i = 0; i < n; i++) {
            if ((nums[i] & rightmostBit) == 0) {
                xor1 ^= nums[i];
            } else {
                xor2 ^= nums[i];
            }
            
            if (((i + 1) & rightmostBit) == 0) {
                xor1 ^= (i + 1);
            } else {
                xor2 ^= (i + 1);
            }
        }
        
        // 4. 确定哪个是重复的数
        for (int num : nums) {
            if (num == xor1) {
                return new int[]{xor1, xor2}; // xor1是重复的数
            }
        }
        
        return new int[]{xor2, xor1}; // xor2是重复的数
    }

    // 测试方法
    public static void main(String[] args) {
        System.out.println("=== 异或运算性质和技巧演示 ===\n");

        // 测试交换两个数
        int[] arr1 = {10, 20};
        System.out.println("交换前: arr1[0]=" + arr1[0] + ", arr1[1]=" + arr1[1]);
        swap(arr1, 0, 1);
        System.out.println("交换后: arr1[0]=" + arr1[0] + ", arr1[1]=" + arr1[1]);
        System.out.println();

        // 测试找到出现奇数次的元素
        int[] arr2 = {1, 2, 3, 2, 1};
        System.out.println("数组 [1,2,3,2,1] 中出现奇数次的元素: " + findOddOccurrence(arr2));
        System.out.println();

        // 测试找到两个出现奇数次的元素
        int[] arr3 = {1, 2, 3, 4, 2, 1};
        int[] result = findTwoOddOccurrences(arr3);
        System.out.println("数组 [1,2,3,4,2,1] 中出现奇数次的两个元素: " + result[0] + ", " + result[1]);
        System.out.println();

        // 测试2的幂判断
        System.out.println("8 是否为2的幂: " + isPowerOfTwo(8));
        System.out.println("10 是否为2的幂: " + isPowerOfTwo(10));
        System.out.println();

        // 测试位操作技巧
        int num = 12; // 二进制: 1100
        System.out.println("数字 " + num + " 的二进制表示: " + Integer.toBinaryString(num));
        System.out.println("最右边的1位: " + getRightmostBit(num) + " (二进制: " + Integer.toBinaryString(getRightmostBit(num)) + ")");
        System.out.println("清除最右边的1位后: " + clearRightmostBit(num) + " (二进制: " + Integer.toBinaryString(clearRightmostBit(num)) + ")");
        System.out.println("1的个数: " + countOnes(num));
        System.out.println("第2位的值: " + getBit(num, 2));
        System.out.println("设置第0位为1: " + setBit(num, 0) + " (二进制: " + Integer.toBinaryString(setBit(num, 0)) + ")");
        System.out.println("清除第3位: " + clearBit(num, 3) + " (二进制: " + Integer.toBinaryString(clearBit(num, 3)) + ")");
        System.out.println("翻转第1位: " + flipBit(num, 1) + " (二进制: " + Integer.toBinaryString(flipBit(num, 1)) + ")");
        System.out.println();

        // 测试实际应用示例
        int[] arr4 = {0, 1, 3};
        System.out.println("数组 [0,1,3] 中缺失的数字: " + findMissingNumber(arr4));
        
        int[] arr5 = {4, 14, 2};
        System.out.println("数组 [4,14,2] 的汉明距离总和: " + totalHammingDistance(arr5));
        
        int[] arr6 = {1, 2, 2, 4};
        int[] errorNums = findErrorNums(arr6);
        System.out.println("数组 [1,2,2,4] 中重复的数字和缺失的数字: [" + errorNums[0] + ", " + errorNums[1] + "]");
    }
}