package class107;

import java.util.*;

/**
 * 哈希算法经典题目集合
 * 
 * 本文件包含各大算法平台（LeetCode、Codeforces、剑指Offer等）的哈希相关经典题目
 * 每个题目都提供详细的注释、复杂度分析和多种解法
 * 
 * 哈希算法核心思想：
 * 1. 使用哈希表实现O(1)时间复杂度的查找、插入和删除
 * 2. 处理哈希冲突的方法：链地址法、开放地址法、再哈希法等
 * 3. 哈希函数设计原则：均匀分布、计算简单、冲突率低
 * 
 * 时间复杂度分析：
 * - 理想情况下：O(1) 查找、插入、删除
 * - 最坏情况下：O(n) 当所有元素哈希到同一位置时
 * 
 * 空间复杂度分析：
 * - 哈希表：O(n) 存储n个元素
 * - 额外空间：O(k) 用于存储辅助信息
 */

public class Code04_HashAlgorithmProblems {
    
    /**
     * LeetCode 1. 两数之和 (Two Sum)
     * 题目来源：https://leetcode.com/problems/two-sum/
     * 题目描述：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
     * 
     * 算法思路：
     * 1. 使用哈希表存储每个数字及其对应的索引
     * 2. 遍历数组时检查 target - nums[i] 是否在哈希表中
     * 3. 如果存在，则返回两个索引
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * 工程化考量：
     * - 异常处理：空数组、无解情况
     * - 边界条件：重复元素、负数、零
     * - 性能优化：一次遍历完成查找
     */
    public static class TwoSumSolution {
        public int[] twoSum(int[] nums, int target) {
            // 输入验证
            if (nums == null || nums.length < 2) {
                throw new IllegalArgumentException("数组长度必须大于等于2");
            }
            
            Map<Integer, Integer> numMap = new HashMap<>();
            
            for (int i = 0; i < nums.length; i++) {
                int complement = target - nums[i];
                
                // 检查补数是否在哈希表中
                if (numMap.containsKey(complement)) {
                    return new int[]{numMap.get(complement), i};
                }
                
                // 将当前数字和索引存入哈希表
                numMap.put(nums[i], i);
            }
            
            throw new IllegalArgumentException("没有找到符合条件的两个数");
        }
        
        /**
         * 两数之和的暴力解法（用于对比）
         * 时间复杂度：O(n²)
         * 空间复杂度：O(1)
         */
        public int[] twoSumBruteForce(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                for (int j = i + 1; j < nums.length; j++) {
                    if (nums[i] + nums[j] == target) {
                        return new int[]{i, j};
                    }
                }
            }
            throw new IllegalArgumentException("没有找到符合条件的两个数");
        }
        
        /**
         * 两数之和的排序双指针解法
         * 时间复杂度：O(n log n)
         * 空间复杂度：O(n)
         */
        public int[] twoSumTwoPointers(int[] nums, int target) {
            // 创建索引数组
            Integer[] indices = new Integer[nums.length];
            for (int i = 0; i < nums.length; i++) {
                indices[i] = i;
            }
            
            // 按数值排序索引
            Arrays.sort(indices, (a, b) -> Integer.compare(nums[a], nums[b]));
            
            int left = 0, right = nums.length - 1;
            while (left < right) {
                int sum = nums[indices[left]] + nums[indices[right]];
                
                if (sum == target) {
                    return new int[]{indices[left], indices[right]};
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
            
            throw new IllegalArgumentException("没有找到符合条件的两个数");
        }
    }
    
    /**
     * LeetCode 49. 字母异位词分组 (Group Anagrams)
     * 题目来源：https://leetcode.com/problems/group-anagrams/
     * 题目描述：给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
     * 
     * 算法思路：
     * 1. 使用排序后的字符串作为哈希表的键
     * 2. 将具有相同排序字符串的单词分组
     * 3. 返回分组后的结果
     * 
     * 时间复杂度：O(n * k log k)，其中n是字符串数量，k是字符串最大长度
     * 空间复杂度：O(n * k)
     * 
     * 优化思路：
     * - 使用字符计数作为键，避免排序开销
     * - 使用质数乘积作为键，减少字符串操作
     */
    public static class GroupAnagramsSolution {
        public List<List<String>> groupAnagrams(String[] strs) {
            if (strs == null || strs.length == 0) {
                return new ArrayList<>();
            }
            
            Map<String, List<String>> anagramMap = new HashMap<>();
            
            for (String str : strs) {
                // 将字符串排序作为键
                char[] charArray = str.toCharArray();
                Arrays.sort(charArray);
                String sortedStr = new String(charArray);
                
                // 添加到对应的分组
                if (!anagramMap.containsKey(sortedStr)) {
                    anagramMap.put(sortedStr, new ArrayList<>());
                }
                anagramMap.get(sortedStr).add(str);
            }
            
            return new ArrayList<>(anagramMap.values());
        }
        
        /**
         * 使用字符计数作为键的优化版本
         * 时间复杂度：O(n * k)
         * 空间复杂度：O(n * k)
         */
        public List<List<String>> groupAnagramsOptimized(String[] strs) {
            if (strs == null || strs.length == 0) {
                return new ArrayList<>();
            }
            
            Map<String, List<String>> anagramMap = new HashMap<>();
            
            for (String str : strs) {
                // 使用字符计数作为键
                String key = getCharacterCountKey(str);
                
                if (!anagramMap.containsKey(key)) {
                    anagramMap.put(key, new ArrayList<>());
                }
                anagramMap.get(key).add(str);
            }
            
            return new ArrayList<>(anagramMap.values());
        }
        
        private String getCharacterCountKey(String str) {
            int[] count = new int[26];
            for (char c : str.toCharArray()) {
                count[c - 'a']++;
            }
            
            StringBuilder keyBuilder = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                if (count[i] > 0) {
                    keyBuilder.append('a' + i).append(count[i]);
                }
            }
            return keyBuilder.toString();
        }
        
        /**
         * 使用质数乘积作为键的优化版本
         * 时间复杂度：O(n * k)
         * 空间复杂度：O(n)
         */
        public List<List<String>> groupAnagramsPrime(String[] strs) {
            if (strs == null || strs.length == 0) {
                return new ArrayList<>();
            }
            
            // 前26个质数
            int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 
                          43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101};
            
            Map<Long, List<String>> anagramMap = new HashMap<>();
            
            for (String str : strs) {
                long product = 1;
                for (char c : str.toCharArray()) {
                    product *= primes[c - 'a'];
                }
                
                if (!anagramMap.containsKey(product)) {
                    anagramMap.put(product, new ArrayList<>());
                }
                anagramMap.get(product).add(str);
            }
            
            return new ArrayList<>(anagramMap.values());
        }
    }
    
    /**
     * LeetCode 242. 有效的字母异位词 (Valid Anagram)
     * 题目来源：https://leetcode.com/problems/valid-anagram/
     * 题目描述：给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
     * 
     * 算法思路：
     * 1. 使用哈希表统计每个字符出现的次数
     * 2. 比较两个字符串的字符频率
     * 3. 如果所有字符频率相同，则是字母异位词
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)，因为字符集大小固定为26
     */
    public static class ValidAnagramSolution {
        public boolean isAnagram(String s, String t) {
            if (s.length() != t.length()) {
                return false;
            }
            
            int[] charCount = new int[26];
            
            // 统计字符串s的字符频率
            for (char c : s.toCharArray()) {
                charCount[c - 'a']++;
            }
            
            // 减去字符串t的字符频率
            for (char c : t.toCharArray()) {
                charCount[c - 'a']--;
                // 如果出现负数，说明t中某个字符比s中多
                if (charCount[c - 'a'] < 0) {
                    return false;
                }
            }
            
            // 检查所有字符频率是否归零
            for (int count : charCount) {
                if (count != 0) {
                    return false;
                }
            }
            
            return true;
        }
        
        /**
         * 使用排序的解法
         * 时间复杂度：O(n log n)
         * 空间复杂度：O(n)
         */
        public boolean isAnagramSort(String s, String t) {
            if (s.length() != t.length()) {
                return false;
            }
            
            char[] sArray = s.toCharArray();
            char[] tArray = t.toCharArray();
            
            Arrays.sort(sArray);
            Arrays.sort(tArray);
            
            return Arrays.equals(sArray, tArray);
        }
        
        /**
         * 使用哈希表的通用解法（支持Unicode字符）
         * 时间复杂度：O(n)
         * 空间复杂度：O(n)
         */
        public boolean isAnagramUnicode(String s, String t) {
            if (s.length() != t.length()) {
                return false;
            }
            
            Map<Character, Integer> charMap = new HashMap<>();
            
            // 统计字符串s的字符频率
            for (char c : s.toCharArray()) {
                charMap.put(c, charMap.getOrDefault(c, 0) + 1);
            }
            
            // 减去字符串t的字符频率
            for (char c : t.toCharArray()) {
                if (!charMap.containsKey(c)) {
                    return false;
                }
                charMap.put(c, charMap.get(c) - 1);
                if (charMap.get(c) == 0) {
                    charMap.remove(c);
                }
            }
            
            return charMap.isEmpty();
        }
    }
    
    /**
     * LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
     * 题目来源：https://leetcode.com/problems/longest-substring-without-repeating-characters/
     * 题目描述：给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
     * 
     * 算法思路：
     * 1. 使用滑动窗口和哈希表记录字符最后出现的位置
     * 2. 维护左右指针，右指针遍历字符串
     * 3. 当遇到重复字符时，移动左指针到重复字符的下一个位置
     * 4. 更新最大长度
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(min(m, n))，其中m是字符集大小
     */
    public static class LongestSubstringSolution {
        public int lengthOfLongestSubstring(String s) {
            if (s == null || s.length() == 0) {
                return 0;
            }
            
            Map<Character, Integer> charIndexMap = new HashMap<>();
            int maxLength = 0;
            int left = 0;
            
            for (int right = 0; right < s.length(); right++) {
                char currentChar = s.charAt(right);
                
                // 如果字符已经存在，并且索引在窗口内
                if (charIndexMap.containsKey(currentChar) && charIndexMap.get(currentChar) >= left) {
                    // 移动左指针到重复字符的下一个位置
                    left = charIndexMap.get(currentChar) + 1;
                }
                
                // 更新字符的最新位置
                charIndexMap.put(currentChar, right);
                
                // 更新最大长度
                maxLength = Math.max(maxLength, right - left + 1);
            }
            
            return maxLength;
        }
        
        /**
         * 使用数组代替哈希表的优化版本（仅适用于ASCII字符）
         * 时间复杂度：O(n)
         * 空间复杂度：O(128)
         */
        public int lengthOfLongestSubstringArray(String s) {
            if (s == null || s.length() == 0) {
                return 0;
            }
            
            int[] charIndex = new int[128]; // ASCII字符集
            Arrays.fill(charIndex, -1);
            
            int maxLength = 0;
            int left = 0;
            
            for (int right = 0; right < s.length(); right++) {
                char currentChar = s.charAt(right);
                
                // 如果字符已经存在，并且索引在窗口内
                if (charIndex[currentChar] >= left) {
                    left = charIndex[currentChar] + 1;
                }
                
                // 更新字符的最新位置
                charIndex[currentChar] = right;
                
                // 更新最大长度
                maxLength = Math.max(maxLength, right - left + 1);
            }
            
            return maxLength;
        }
    }
    
    /**
     * LeetCode 560. 和为K的子数组 (Subarray Sum Equals K)
     * 题目来源：https://leetcode.com/problems/subarray-sum-equals-k/
     * 题目描述：给你一个整数数组 nums 和一个整数 k ，请你统计并返回该数组中和为 k 的连续子数组的个数。
     * 
     * 算法思路：
     * 1. 使用前缀和和哈希表
     * 2. 记录每个前缀和出现的次数
     * 3. 对于当前前缀和sum，检查sum - k是否在哈希表中
     * 4. 如果存在，则说明存在子数组和为k
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static class SubarraySumSolution {
        public int subarraySum(int[] nums, int k) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            
            Map<Integer, Integer> prefixSumCount = new HashMap<>();
            prefixSumCount.put(0, 1); // 前缀和为0出现1次
            
            int count = 0;
            int prefixSum = 0;
            
            for (int num : nums) {
                prefixSum += num;
                
                // 检查是否存在前缀和等于prefixSum - k
                if (prefixSumCount.containsKey(prefixSum - k)) {
                    count += prefixSumCount.get(prefixSum - k);
                }
                
                // 更新当前前缀和的出现次数
                prefixSumCount.put(prefixSum, prefixSumCount.getOrDefault(prefixSum, 0) + 1);
            }
            
            return count;
        }
        
        /**
         * 暴力解法（用于对比）
         * 时间复杂度：O(n²)
         * 空间复杂度：O(1)
         */
        public int subarraySumBruteForce(int[] nums, int k) {
            int count = 0;
            for (int i = 0; i < nums.length; i++) {
                int sum = 0;
                for (int j = i; j < nums.length; j++) {
                    sum += nums[j];
                    if (sum == k) {
                        count++;
                    }
                }
            }
            return count;
        }
    }
    
    /**
     * 剑指Offer 50. 第一个只出现一次的字符
     * 题目来源：剑指Offer面试题50
     * 题目描述：在字符串s中找出第一个只出现一次的字符
     * 
     * 算法思路：
     * 1. 使用哈希表统计每个字符出现的次数
     * 2. 再次遍历字符串，找到第一个出现次数为1的字符
     * 3. 返回该字符
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)，因为字符集大小固定
     */
    public static class FirstUniqueCharSolution {
        public char firstUniqChar(String s) {
            if (s == null || s.length() == 0) {
                return ' ';
            }
            
            int[] charCount = new int[256]; // 扩展ASCII字符集
            
            // 第一次遍历：统计字符频率
            for (char c : s.toCharArray()) {
                charCount[c]++;
            }
            
            // 第二次遍历：找到第一个唯一字符
            for (char c : s.toCharArray()) {
                if (charCount[c] == 1) {
                    return c;
                }
            }
            
            return ' ';
        }
        
        /**
         * 使用LinkedHashMap保持插入顺序的解法
         * 时间复杂度：O(n)
         * 空间复杂度：O(n)
         */
        public char firstUniqCharLinkedHashMap(String s) {
            if (s == null || s.length() == 0) {
                return ' ';
            }
            
            Map<Character, Integer> charMap = new LinkedHashMap<>();
            
            // 统计字符频率，保持插入顺序
            for (char c : s.toCharArray()) {
                charMap.put(c, charMap.getOrDefault(c, 0) + 1);
            }
            
            // 找到第一个出现次数为1的字符
            for (Map.Entry<Character, Integer> entry : charMap.entrySet()) {
                if (entry.getValue() == 1) {
                    return entry.getKey();
                }
            }
            
            return ' ';
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 哈希算法经典题目测试 ===\n");
        
        // 测试两数之和
        System.out.println("1. 两数之和测试:");
        TwoSumSolution twoSum = new TwoSumSolution();
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;
        int[] result1 = twoSum.twoSum(nums1, target1);
        System.out.println("数组: " + Arrays.toString(nums1) + ", 目标: " + target1);
        System.out.println("结果: [" + result1[0] + ", " + result1[1] + "]");
        
        // 测试字母异位词分组
        System.out.println("\n2. 字母异位词分组测试:");
        GroupAnagramsSolution groupAnagrams = new GroupAnagramsSolution();
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> anagramGroups = groupAnagrams.groupAnagrams(strs);
        System.out.println("输入: " + Arrays.toString(strs));
        System.out.println("分组结果: " + anagramGroups);
        
        // 测试有效的字母异位词
        System.out.println("\n3. 有效的字母异位词测试:");
        ValidAnagramSolution validAnagram = new ValidAnagramSolution();
        String s1 = "anagram", s2 = "nagaram";
        boolean isAnagram = validAnagram.isAnagram(s1, s2);
        System.out.println("s1 = \"" + s1 + "\", s2 = \"" + s2 + "\"");
        System.out.println("是否是字母异位词: " + isAnagram);
        
        // 测试无重复字符的最长子串
        System.out.println("\n4. 无重复字符的最长子串测试:");
        LongestSubstringSolution longestSubstring = new LongestSubstringSolution();
        String testStr = "abcabcbb";
        int maxLength = longestSubstring.lengthOfLongestSubstring(testStr);
        System.out.println("字符串: \"" + testStr + "\"");
        System.out.println("最长无重复子串长度: " + maxLength);
        
        // 测试和为K的子数组
        System.out.println("\n5. 和为K的子数组测试:");
        SubarraySumSolution subarraySum = new SubarraySumSolution();
        int[] nums2 = {1, 1, 1};
        int k = 2;
        int subarrayCount = subarraySum.subarraySum(nums2, k);
        System.out.println("数组: " + Arrays.toString(nums2) + ", k = " + k);
        System.out.println("和为" + k + "的子数组个数: " + subarrayCount);
        
        // 测试第一个只出现一次的字符
        System.out.println("\n6. 第一个只出现一次的字符测试:");
        FirstUniqueCharSolution firstUniqueChar = new FirstUniqueCharSolution();
        String testStr2 = "leetcode";
        char uniqueChar = firstUniqueChar.firstUniqChar(testStr2);
        System.out.println("字符串: \"" + testStr2 + "\"");
        System.out.println("第一个只出现一次的字符: " + uniqueChar);
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("1. 两数之和: O(n) 时间, O(n) 空间");
        System.out.println("2. 字母异位词分组: O(n*k log k) 时间, O(n*k) 空间");
        System.out.println("3. 有效的字母异位词: O(n) 时间, O(1) 空间");
        System.out.println("4. 无重复字符的最长子串: O(n) 时间, O(min(m,n)) 空间");
        System.out.println("5. 和为K的子数组: O(n) 时间, O(n) 空间");
        System.out.println("6. 第一个只出现一次的字符: O(n) 时间, O(1) 空间");
        
        System.out.println("\n=== 工程化建议 ===");
        System.out.println("1. 选择合适的哈希函数，平衡计算复杂度和冲突率");
        System.out.println("2. 根据数据规模选择合适的哈希表实现（HashMap、ConcurrentHashMap等）");
        System.out.println("3. 注意哈希表的负载因子和扩容机制");
        System.out.println("4. 在多线程环境下使用线程安全的哈希表实现");
        System.out.println("5. 对于小规模数据，可以考虑使用数组代替哈希表");
        System.out.println("6. 注意哈希碰撞攻击的防护");
    }
}