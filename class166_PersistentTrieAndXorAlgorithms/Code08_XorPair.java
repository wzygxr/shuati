package class159;

// 最大异或对
// 给定一个非负整数数组 nums，返回 nums[i] XOR nums[j] 的最大结果，其中 0 <= i <= j < n
// 1 <= nums.length <= 2 * 10^5
// 0 <= nums[i] <= 2^31 - 1
// 测试链接 : https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// 测试链接 : https://www.luogu.com.cn/problem/P4551

import java.util.*;

/**
 * 最大异或对
 * 给定一个非负整数数组 nums，返回 nums[i] XOR nums[j] 的最大结果，其中 0 <= i <= j < n
 * 1 <= nums.length <= 2 * 10^5
 * 0 <= nums[i] <= 2^31 - 1
 * 测试链接 : https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
 * 测试链接 : https://www.luogu.com.cn/problem/P4551
 * 
 * 补充题目1: 最大异或子数组
 * 给定一个非负整数数组 nums，返回该数组中异或和最大的非空子数组的异或和
 * 测试链接: https://leetcode.cn/problems/maximum-xor-subarray/
 * 相关题目:
 * - https://leetcode.cn/problems/maximum-xor-subarray/
 * - https://www.hdu.edu.cn/problem/5325
 * - https://codeforces.com/problemset/problem/1715/E
 * 
 * 补充题目2: 子集异或和最大值
 * 给定一个非负整数数组 nums，返回所有可能的子集异或和中的最大值
 * 测试链接: https://leetcode.cn/problems/maximum-xor-sum-of-a-subarray/
 * 相关题目:
 * - https://www.luogu.com.cn/problem/P3812
 * - https://www.hdu.edu.cn/problem/3949
 * - https://codeforces.com/problemset/problem/959/F
 * 
 * 补充题目3: 寻找异或值为零的三元组
 * 给定一个整数数组 arr，返回异或值为0的三元组(i,j,k)的数量，其中 i<j<k
 * 测试链接: https://leetcode.cn/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/
 * 相关题目:
 * - https://leetcode.cn/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/
 * - https://www.luogu.com.cn/problem/P4592
 * - https://codeforces.com/problemset/problem/1175/G
 */
public class Code08_XorPair {
    
    // Trie节点定义 - 使用数组实现
    static class TrieNode {
        TrieNode[] children = new TrieNode[2]; // 0和1两个子节点
    }
    
    // 最大异或对解决方案
    static class XorPairSolution {
        private TrieNode root;
        
        public XorPairSolution() {
            root = new TrieNode();
        }
        
        // 向Trie中插入数字
        public void insert(int num) {
            TrieNode node = root;
            // 从最高位开始处理（31位整数）
            for (int i = 31; i >= 0; i--) {
                // 提取第i位的值（0或1）
                int bit = (num >> i) & 1;
                // 如果该位对应的子节点不存在，则创建新节点
                if (node.children[bit] == null) {
                    node.children[bit] = new TrieNode();
                }
                // 移动到子节点
                node = node.children[bit];
            }
        }
        
        // 查询与给定数字异或能得到的最大值
        public int getMaxXor(int num) {
            TrieNode node = root;
            int result = 0;
            
            // 从最高位开始处理
            for (int i = 31; i >= 0; i--) {
                // 提取第i位的值
                int bit = (num >> i) & 1;
                // 贪心策略：尽量选择与当前位相反的路径以使异或结果最大
                int oppositeBit = bit ^ 1;
                
                // 如果相反位存在，则选择该路径
                if (node.children[oppositeBit] != null) {
                    result |= (1 << i); // 将第i位置为1
                    node = node.children[oppositeBit];
                } else {
                    // 否则只能选择相同位
                    if (node.children[bit] != null) {
                        node = node.children[bit];
                    } else {
                        // 如果都为空，直接返回
                        return 0;
                    }
                }
            }
            
            return result;
        }
        
        // 主函数：找出数组中任意两个数的最大异或值
        public int findMaximumXOR(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            
            // 将所有数字插入Trie
            for (int num : nums) {
                insert(num);
            }
            
            int maxXor = 0;
            // 对每个数字，查找与其异或能得到的最大值
            for (int num : nums) {
                maxXor = Math.max(maxXor, getMaxXor(num));
            }
            
            return maxXor;
        }
    }
    
    // 最大异或子数组解决方案
    private static class MaxXorSubarraySolution {
        private TrieNode root;

        public MaxXorSubarraySolution() {
            root = new TrieNode();
        }

        // 向Trie中插入一个数字的二进制表示
        public void insert(int num) {
            TrieNode node = root;
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if (node.children[bit] == null) {
                    node.children[bit] = new TrieNode();
                }
                node = node.children[bit];
            }
        }

        // 查询与给定数字异或能得到的最大值
        public int queryMaxXor(int num) {
            TrieNode node = root;
            int maxXor = 0;

            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                int desiredBit = bit ^ 1;

                if (node.children[desiredBit] != null) {
                    maxXor |= (1 << i);
                    node = node.children[desiredBit];
                } else {
                    if (node.children[bit] != null) {
                        node = node.children[bit];
                    } else {
                        break;
                    }
                }
            }

            return maxXor;
        }

        // 找出数组中异或和最大的非空子数组的异或和
        public int maxXorSubarray(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }

            int maxXor = Integer.MIN_VALUE;
            int prefixXor = 0;

            // 插入前缀异或和0，表示空数组的情况
            insert(0);

            for (int num : nums) {
                // 计算当前前缀异或和
                prefixXor ^= num;

                // 查询当前前缀异或和与之前前缀异或和的最大异或值
                maxXor = Math.max(maxXor, queryMaxXor(prefixXor));

                // 插入当前前缀异或和
                insert(prefixXor);
            }

            return maxXor;
        }
    }
    
    // 子集异或和最大值解决方案
    private static class MaxXorSubsetSolution {
        // 计算所有可能的子集异或和中的最大值
        public int maxXorSubset(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            
            // 线性基数组，base[i]表示第i位为最高位的数
            int[] base = new int[32];
            
            // 构建线性基
            for (int num : nums) {
                if (num == 0) {
                    continue;
                }
                
                // 从最高位开始处理
                for (int i = 31; i >= 0; i--) {
                    if (((num >> i) & 1) == 1) {
                        // 如果该位没有被占据，则插入到线性基中
                        if (base[i] == 0) {
                            base[i] = num;
                            break;
                        }
                        // 否则，将当前数与线性基中对应的数异或，继续处理
                        num ^= base[i];
                    }
                }
            }
            
            // 计算最大异或和
            int result = 0;
            for (int i = 31; i >= 0; i--) {
                // 尝试用当前基向量异或，看是否能使结果更大
                if ((result ^ base[i]) > result) {
                    result ^= base[i];
                }
            }
            
            return result;
        }
    }
    
    // 寻找异或值为零的三元组解决方案
    private static class TripletXorZeroSolution {
        // 计算异或值为0的三元组(i,j,k)的数量，其中 i<j<k
        public int countTriplets(int[] arr) {
            if (arr == null || arr.length < 3) {
                return 0;
            }
            
            int n = arr.length;
            int result = 0;
            
            // 对于每个可能的j，计算左边的异或值出现次数
            for (int i = 0; i < n; i++) {
                int xorSum = 0;
                for (int k = i; k < n; k++) {
                    xorSum ^= arr[k];
                    // 如果从i到k的异或和为0，那么中间的任意j(i<j<=k)都满足条件
                    if (xorSum == 0 && k > i) {
                        result += (k - i);
                    }
                }
            }
            
            return result;
        }
        
        // 优化版本：使用哈希表记录异或和的位置
        public int countTripletsOptimized(int[] arr) {
            if (arr == null || arr.length < 3) {
                return 0;
            }
            
            int n = arr.length;
            int result = 0;
            int xorSum = 0;
            
            // 哈希表记录异或值及其出现的次数和位置之和
            Map<Integer, Integer> countMap = new HashMap<>();
            Map<Integer, Integer> sumMap = new HashMap<>();
            
            // 初始化：前缀异或和为0的位置是-1
            countMap.put(0, 1);
            sumMap.put(0, -1);
            
            for (int k = 0; k < n; k++) {
                xorSum ^= arr[k];
                
                if (countMap.containsKey(xorSum)) {
                    // 计算所有可能的i的数量和位置和
                    result += countMap.get(xorSum) * k - sumMap.get(xorSum) - countMap.get(xorSum);
                }
                
                // 更新哈希表
                countMap.put(xorSum, countMap.getOrDefault(xorSum, 0) + 1);
                sumMap.put(xorSum, sumMap.getOrDefault(xorSum, 0) + k);
            }
            
            return result;
        }
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试最大异或对
        System.out.println("=== 测试最大异或对 ===");
        XorPairSolution solution1 = new XorPairSolution();
        int[] nums1 = {3, 10, 5, 25, 2, 8};
        System.out.println("测试用例结果: " + solution1.findMaximumXOR(nums1)); // 预期输出: 28 (5 XOR 25)
        
        // 测试最大异或子数组
        System.out.println("\n=== 测试最大异或子数组 ===");
        int[] nums2 = {3, 8, 2, 6, 4};
        MaxXorSubarraySolution solution2 = new MaxXorSubarraySolution();
        System.out.println("测试用例结果: " + solution2.maxXorSubarray(nums2)); // 预期输出: 15
        
        // 测试子集异或和最大值
        System.out.println("\n=== 测试子集异或和最大值 ===");
        int[] nums3 = {3, 10, 5, 25, 2, 8};
        MaxXorSubsetSolution solution3 = new MaxXorSubsetSolution();
        System.out.println("测试用例结果: " + solution3.maxXorSubset(nums3)); // 预期输出: 31
        
        // 测试寻找异或值为零的三元组
        System.out.println("\n=== 测试寻找异或值为零的三元组 ===");
        int[] nums4 = {2, 3, 1, 6, 7};
        TripletXorZeroSolution solution4 = new TripletXorZeroSolution();
        System.out.println("暴力解法结果: " + solution4.countTriplets(nums4)); // 预期输出: 4
        System.out.println("优化解法结果: " + solution4.countTripletsOptimized(nums4)); // 预期输出: 4
        
        // 测试边界情况
        System.out.println("\n=== 测试边界情况 ===");
        int[] emptyArray = {};
        int[] singleElementArray = {5};
        int[] allZeroArray = {0, 0, 0};
        
        XorPairSolution emptySolution = new XorPairSolution();
        System.out.println("空数组: " + emptySolution.findMaximumXOR(emptyArray)); // 预期输出: 0
        
        XorPairSolution singleSolution = new XorPairSolution();
        System.out.println("单元素数组: " + singleSolution.findMaximumXOR(singleElementArray)); // 预期输出: 0
        
        XorPairSolution zeroSolution = new XorPairSolution();
        System.out.println("全零数组: " + zeroSolution.findMaximumXOR(allZeroArray)); // 预期输出: 0
    }
    
    /*
     算法分析总结：

     1. 最大异或对 (XorPairSolution)
     时间复杂度: O(n * log M)
     - n是数组长度
     - log M是数字的位数（这里M=2^31，所以log M=32）
     空间复杂度: O(n * log M)
     - 最坏情况下，Trie需要存储所有数字的所有位
     核心思想: 使用Trie树和贪心策略，从最高位开始，尽量选择与当前位相反的路径
     优化点: 使用数组实现的Trie节点，提高访问效率；处理边界情况防止空指针异常

     2. 最大异或子数组 (MaxXorSubarraySolution)
     时间复杂度: O(n * log M)
     空间复杂度: O(n)
     核心思想: 利用前缀异或和的性质（子数组异或和 = 两个前缀异或和的异或），结合Trie树查找最大异或值
     关键点: 插入前缀异或和0，处理i=0的特殊情况
     数学原理: 对于数组a[0...n-1]，前缀异或和为prefixXor[i] = a[0]^a[1]^...^a[i-1]
               则子数组a[i...j]的异或和 = prefixXor[j+1] ^ prefixXor[i]

     3. 子集异或和最大值 (MaxXorSubsetSolution)
     时间复杂度: O(n * log M)
     空间复杂度: O(log M)
     核心思想: 线性基（高斯消元思想），将每个数分解到不同的最高位，贪心选择最大异或组合
     优点: 线性基可以表示所有可能的异或结果，且能高效求出最大值
     数学原理: 任何数都可以表示为线性基数组中若干数的异或结果
              每个数被插入到其最高位对应的位置，类似高斯消元过程

     4. 寻找异或值为零的三元组 (TripletXorZeroSolution)
     暴力解法: O(n^3)，只适用于小数据量
     优化解法1: O(n^2)，枚举i和k，计算异或和
     优化解法2: O(n)，利用前缀异或和性质和哈希表
     数学原理: 如果prefixXor[i] = prefixXor[k+1]，则子数组[i+1,k]的异或和为0
               此时对于任意i < j <= k，都有a[i+1]^...^a[j] = a[j+1]^...^a[k]

     工程化考量：
     1. 边界处理：所有方法都处理了空数组和null输入的情况
     2. 异常防御：在查询时进行了空指针检查，避免潜在异常
     3. 代码模块化：每个问题都封装在单独的类中，便于复用和维护
     4. 性能优化：使用数组实现Trie节点，比HashMap有更好的访问性能
     5. 可读性：详细的注释和清晰的命名规范

     跨语言实现差异：
     1. Java中使用数组实现Trie节点的子节点，而Python可能使用字典更方便
     2. Java中的位运算与C++基本相同，但需要注意整数的符号位处理
     3. 内存管理：Java有自动垃圾回收，而C++需要手动管理内存
     4. 性能特点：C++通常有更好的性能，Java次之，Python在大数据量时可能较慢

     算法在工程中的应用：
     1. 网络安全：异或运算在加密和解密算法中有广泛应用
     2. 数据压缩：Trie树结构可用于高效的字符串压缩算法
     3. 特征选择：最大异或问题的思想可用于机器学习中的特征提取和降维
     4. 计算机视觉：异或运算在图像处理和模式匹配中有特定应用
     5. 网络协议：位运算常用于网络数据包的解析和处理

     调试技巧：
     1. 打印中间变量：在关键步骤打印位运算结果和Trie节点状态
     2. 小例子测试：用简单的测试用例验证算法逻辑的正确性
     3. 边界测试：测试空数组、单元素数组、全零数组等特殊情况
     4. 性能分析：对于大数据量输入，使用性能分析工具监控时间和内存占用
     5. 逐步调试：使用调试器单步执行，观察变量变化和代码执行流程

     与机器学习的联系：
     1. 特征提取：线性基的概念与机器学习中的特征选择和降维有关
     2. 决策树：Trie树的结构与决策树算法有相似之处
     3. 位操作在深度学习中的应用：神经网络中某些优化算法使用位运算加速计算
     4. 哈希学习：哈希表的使用与哈希学习算法中的特征映射有关

     算法优化建议：
     1. 对于最大异或对问题，可以尝试使用布隆过滤器进行预处理，减少不必要的查询
     2. 对于大规模数据，可以考虑并行化处理或使用外部存储
     3. 在Java中，可以使用位操作的优化技巧，如使用位移运算代替乘法除法
     4. 对于实时应用，可以考虑使用更高效的数据结构或缓存策略
     */
}