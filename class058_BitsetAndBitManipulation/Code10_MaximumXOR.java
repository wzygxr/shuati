package class032;

import java.util.HashSet;
import java.util.Set;

// LeetCode 421. 数组中两个数的最大异或值
// 题目链接: https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/
// 题目大意:
// 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
// 
// 进阶：你可以在 O(n) 的时间解决这个问题吗？
// 
// 示例 1:
// 输入: nums = [3,10,5,25,2,8]
// 输出: 28
// 解释: 最大的结果是 5 XOR 25 = 28.
// 
// 示例 2:
// 输入: nums = [0]
// 输出: 0
// 
// 示例 3:
// 输入: nums = [2,4]
// 输出: 6
// 
// 示例 4:
// 输入: nums = [8,10,2]
// 输出: 10
// 
// 示例 5:
// 输入: nums = [14,70,53,83,49,91,36,80,92,51,66,70]
// 输出: 127
// 
// 提示：
// 1 <= nums.length <= 2 * 10^4
// 0 <= nums[i] <= 2^31 - 1
//
// 解题思路:
// 方法一：暴力解法（O(n²)时间复杂度，不推荐）
// 方法二：前缀树（字典树）优化的位运算方法（O(n)时间复杂度）
// 方法三：基于异或性质的位运算方法（O(n)时间复杂度）
//
// 这里我们主要实现方法二和方法三，它们都是基于位运算的高效解法

public class Code10_MaximumXOR {

    // 方法一：暴力解法
    // 时间复杂度: O(n²)
    // 空间复杂度: O(1)
    // 参数nums: 输入的整数数组
    // 返回值: 数组中任意两个数异或的最大值
    public static int findMaximumXOR1(int[] nums) {
        // 初始化最大异或结果为0
        int maxResult = 0;
        
        // 遍历所有可能的数对
        // 外层循环遍历第一个数
        for (int i = 0; i < nums.length; i++) {
            // 内层循环遍历第二个数（j > i避免重复计算）
            for (int j = i + 1; j < nums.length; j++) {
                // 计算当前两个数的异或值
                // ^ 表示按位异或操作
                int currentXOR = nums[i] ^ nums[j];
                // 更新最大异或结果
                // Math.max返回两个数中的较大值
                maxResult = Math.max(maxResult, currentXOR);
            }
        }
        
        // 返回最大异或结果
        return maxResult;
    }
    
    // 方法二：基于位运算和集合的方法
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // 参数nums: 输入的整数数组
    // 返回值: 数组中任意两个数异或的最大值
    public static int findMaximumXOR2(int[] nums) {
        // 初始化最大异或结果为0
        int maxResult = 0;
        // 初始化掩码为0，用于提取数字的前缀
        int mask = 0;
        
        // 从最高位到最低位依次确定结果的每一位
        // 31位整数，但符号位为0（因为题目中nums[i] >= 0），所以从第30位开始
        for (int i = 30; i >= 0; i--) {
            // 构建当前位的掩码
            // mask |= (1 << i) 将mask的第i位设置为1
            // 这样mask就包含了从最高位到当前位的所有位
            mask |= (1 << i);
            
            // 存储所有数在当前掩码下的前缀
            // 使用HashSet存储前缀，便于快速查找
            Set<Integer> prefixes = new HashSet<>();
            // 遍历数组中的每个数
            for (int num : nums) {
                // num & mask 提取num在当前掩码下的前缀
                // 例如：如果mask是11100000，那么num & mask就提取num的高3位
                prefixes.add(num & mask);
            }
            
            // 假设当前位为1，构造可能的最大值
            // maxResult | (1 << i) 将maxResult的第i位设置为1
            // 这是我们希望得到的最大异或结果
            int tempMax = maxResult | (1 << i);
            
            // 检查是否存在两个数，它们的前缀异或等于tempMax
            // 遍历所有前缀
            for (int prefix : prefixes) {
                // 如果prefix ^ target = tempMax，那么target = prefix ^ tempMax
                // 这是基于异或运算的性质：如果a ^ b = c，那么a ^ c = b
                // 我们要找是否存在另一个前缀target，使得prefix ^ target = tempMax
                if (prefixes.contains(prefix ^ tempMax)) {
                    // 找到可行的解，说明最大异或结果的当前位可以为1
                    // 设置当前位为1
                    maxResult = tempMax;
                    // 找到解后跳出循环，继续处理下一位
                    break;
                }
            }
            
            // 如果没有找到，当前位保持为0（maxResult不变）
            // 这是因为我们初始化tempMax时已经将maxResult的当前位设为1
            // 如果找不到匹配的前缀对，说明当前位必须为0
        }
        
        // 返回最大异或结果
        return maxResult;
    }
    
    // 方法三：前缀树（字典树）方法
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // 参数nums: 输入的整数数组
    // 返回值: 数组中任意两个数异或的最大值
    public static int findMaximumXOR3(int[] nums) {
        // 参数验证
        // 检查输入数组是否为null或长度为0
        if (nums == null || nums.length == 0) {
            // 如果数组为空，返回0
            return 0;
        }
        
        // 构建前缀树
        // 创建前缀树的根节点
        TrieNode root = new TrieNode();
        // 遍历数组中的每个数，将其插入到前缀树中
        for (int num : nums) {
            // 调用insert方法将num插入到前缀树中
            insert(root, num);
        }
        
        // 初始化最大异或结果为0
        int maxResult = 0;
        
        // 对于每个数，在前缀树中寻找能产生最大异或值的另一个数
        // 遍历数组中的每个数
        for (int num : nums) {
            // 调用search方法在前缀树中查找与num异或结果最大的数
            // Math.max更新最大异或结果
            maxResult = Math.max(maxResult, search(root, num));
        }
        
        // 返回最大异或结果
        return maxResult;
    }
    
    // 前缀树节点定义
    // 前缀树节点用于存储二进制数的每一位
    private static class TrieNode {
        // children数组存储0和1两个子节点
        // children[0]表示当前位为0的子节点
        // children[1]表示当前位为1的子节点
        TrieNode[] children;
        
        // 构造函数
        public TrieNode() {
            // 初始化children数组，大小为2
            children = new TrieNode[2];
        }
    }
    
    // 向前缀树中插入一个数
    // 参数root: 前缀树的根节点
    // 参数num: 要插入的数
    private static void insert(TrieNode root, int num) {
        // 从根节点开始
        TrieNode node = root;
        
        // 从最高位到最低位插入
        // 31位整数，忽略符号位，所以从第30位开始到第0位
        for (int i = 30; i >= 0; i--) {
            // 提取num的第i位
            // (num >> i) & 1 将num右移i位，然后与1进行按位与操作
            // 这样可以提取num的第i位（0或1）
            int bit = (num >> i) & 1;
            
            // 如果当前位对应的子节点不存在，则创建新节点
            if (node.children[bit] == null) {
                // 创建新的子节点
                node.children[bit] = new TrieNode();
            }
            
            // 移动到子节点
            node = node.children[bit];
        }
    }
    
    // 在已有的前缀树中查找与给定数异或结果最大的数
    // 参数root: 前缀树的根节点
    // 参数num: 给定的数
    // 返回值: num与前缀树中某个数异或的最大值
    private static int search(TrieNode root, int num) {
        // 从根节点开始
        TrieNode node = root;
        // 初始化异或结果为0
        int xor = 0;
        
        // 从最高位到最低位查找
        // 31位整数，忽略符号位，所以从第30位开始到第0位
        for (int i = 30; i >= 0; i--) {
            // 提取num的第i位
            int bit = (num >> i) & 1;
            // 寻找相反的位以最大化异或结果
            // 1 - bit 可以得到bit的相反值（0变1，1变0）
            int desiredBit = 1 - bit;
            
            // 如果存在相反的位
            if (node.children[desiredBit] != null) {
                // 可以找到相反的位，异或结果的当前位为1
                // xor |= (1 << i) 将xor的第i位设置为1
                xor |= (1 << i);
                // 移动到相反位对应的子节点
                node = node.children[desiredBit];
            } else {
                // 找不到相反的位，只能使用相同的位
                // 移动到相同位对应的子节点
                node = node.children[bit];
            }
        }
        
        // 返回异或结果
        return xor;
    }
    
    // 工程化改进版本：增加参数验证和异常处理
    // 参数nums: 输入的整数数组
    // 返回值: 数组中任意两个数异或的最大值
    public static int findMaximumXORWithValidation(int[] nums) {
        try {
            // 参数验证
            // 检查输入数组是否为null
            if (nums == null) {
                // 抛出异常，说明输入参数不能为null
                throw new IllegalArgumentException("Input array cannot be null");
            }
            
            // 边界情况处理
            // 检查数组长度
            if (nums.length <= 1) {
                // 如果数组只有一个元素或为空，最大异或值为0
                return nums.length == 1 ? 0 : 0;
            }
            
            // 使用方法二实现
            // 调用findMaximumXOR2方法计算最大异或值
            return findMaximumXOR2(nums);
        } catch (Exception e) {
            // 记录异常日志（在实际应用中）
            // 在生产环境中，应该使用日志框架记录异常
            System.err.println("Error in findMaximumXOR: " + e.getMessage());
            // 异常情况下返回0
            return 0;
        }
    }
    
    // 单元测试方法
    // 验证各种实现方法的正确性
    public static void runTests() {
        // 测试用例1
        int[] nums1 = {3, 10, 5, 25, 2, 8};
        System.out.println("Test 1: [3,10,5,25,2,8] -> " + findMaximumXOR2(nums1) + " (Expected: 28)");
        
        // 测试用例2
        int[] nums2 = {0};
        System.out.println("Test 2: [0] -> " + findMaximumXOR2(nums2) + " (Expected: 0)");
        
        // 测试用例3
        int[] nums3 = {2, 4};
        System.out.println("Test 3: [2,4] -> " + findMaximumXOR2(nums3) + " (Expected: 6)");
        
        // 测试用例4
        int[] nums4 = {8, 10, 2};
        System.out.println("Test 4: [8,10,2] -> " + findMaximumXOR2(nums4) + " (Expected: 10)");
        
        // 测试用例5
        int[] nums5 = {14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70};
        System.out.println("Test 5: [14,70,53,83,49,91,36,80,92,51,66,70] -> " + 
                          findMaximumXOR2(nums5) + " (Expected: 127)");
        
        // 测试不同方法的结果一致性
        System.out.println("\nMethod comparison:");
        System.out.println("Test 1 results:");
        System.out.println("  findMaximumXOR1: " + findMaximumXOR1(nums1));
        System.out.println("  findMaximumXOR2: " + findMaximumXOR2(nums1));
        System.out.println("  findMaximumXOR3: " + findMaximumXOR3(nums1));
        System.out.println("  findMaximumXORWithValidation: " + findMaximumXORWithValidation(nums1));
    }
    
    // 性能测试方法
    // 测试不同方法的性能
    public static void performanceTest() {
        // 生成大规模测试数据
        // 创建一个包含10000个随机整数的数组
        int[] largeNums = new int[10000];
        for (int i = 0; i < largeNums.length; i++) {
            // 生成0到Integer.MAX_VALUE之间的随机整数
            largeNums[i] = (int)(Math.random() * Integer.MAX_VALUE);
        }
        
        // 测试方法一时间（仅在小规模数据上测试，大规模数据会超时）
        int[] smallNums = {3, 10, 5, 25, 2, 8};
        // 记录开始时间
        long startTime = System.nanoTime();
        // 调用方法一计算结果
        int result1 = findMaximumXOR1(smallNums);
        // 记录结束时间
        long endTime = System.nanoTime();
        System.out.println("\nPerformance of findMaximumXOR1: " + 
                          (endTime - startTime) + " ns, Result: " + result1);
        
        // 测试方法二时间
        // 记录开始时间
        startTime = System.nanoTime();
        // 调用方法二计算结果
        int result2 = findMaximumXOR2(largeNums);
        // 记录结束时间
        endTime = System.nanoTime();
        System.out.println("Performance of findMaximumXOR2: " + 
                          (endTime - startTime) / 1000 + " μs, Result: " + result2);
        
        // 测试方法三时间
        // 记录开始时间
        startTime = System.nanoTime();
        // 调用方法三计算结果
        int result3 = findMaximumXOR3(largeNums);
        // 记录结束时间
        endTime = System.nanoTime();
        System.out.println("Performance of findMaximumXOR3: " + 
                          (endTime - startTime) / 1000 + " μs, Result: " + result3);
    }
    
    // 主函数，程序入口点
    public static void main(String[] args) {
        System.out.println("LeetCode 421. 数组中两个数的最大异或值");
        System.out.println("使用位运算优化实现");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        System.out.println("\n复杂度分析:");
        System.out.println("方法一（暴力解法）:");
        System.out.println("  时间复杂度: O(n²)，其中n是数组长度");
        System.out.println("  空间复杂度: O(1)");
        System.out.println("  优点: 实现简单");
        System.out.println("  缺点: 对于大数组效率低");
        
        System.out.println("\n方法二（基于位运算和集合）:");
        System.out.println("  时间复杂度: O(n)，每个位处理需要O(n)时间，总共处理32个位");
        System.out.println("  空间复杂度: O(n)，用于存储前缀集合");
        System.out.println("  优点: 时间效率高，实现相对简单");
        
        System.out.println("\n方法三（前缀树）:");
        System.out.println("  时间复杂度: O(n)，构建树和查询都是O(n)时间");
        System.out.println("  空间复杂度: O(n)，用于存储前缀树");
        System.out.println("  优点: 位操作思想清晰，扩展性好");
        
        System.out.println("\n适用场景总结:");
        System.out.println("1. 对于小数组，可以使用暴力解法");
        System.out.println("2. 对于大数组，应使用方法二或方法三，它们的时间复杂度都是O(n)");
        System.out.println("3. 在工程实践中，方法二实现更简洁，而方法三更能体现位运算的思想");
    }
}