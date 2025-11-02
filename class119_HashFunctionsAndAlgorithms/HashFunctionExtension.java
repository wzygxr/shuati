package class106;

import java.util.*;

public class HashFunctionExtension {
    /**
     * LeetCode 36. Valid Sudoku (有效的数独)
     * 题目来源: https://leetcode.com/problems/valid-sudoku/
     * 
     * 题目描述:
     * 请你判断一个 9 x 9 的数独是否有效。只需要根据以下规则，验证已经填入的数字是否有效即可。
     * 1. 数字 1-9 在每一行只能出现一次。
     * 2. 数字 1-9 在每一列只能出现一次。
     * 3. 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
     * 
     * 注意:
     * - 一个有效的数独（部分已填充）不一定是可解的。
     * - 只需要根据以上规则，验证已经填入的数字是否有效即可。
     * - 空白格用 '.' 表示。
     * 
     * 算法思路:
     * 使用哈希表记录每行、每列、每个小方块中已经出现的数字
     * 
     * 时间复杂度: O(1)，因为数独大小固定
     * 空间复杂度: O(1)
     */
    public boolean isValidSudoku(char[][] board) {
        // 创建3个哈希表数组，分别存储每行、每列和每个小方块中出现的数字
        HashSet<Character>[] rows = new HashSet[9];
        HashSet<Character>[] cols = new HashSet[9];
        HashSet<Character>[] boxes = new HashSet[9];
        
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char num = board[i][j];
                if (num == '.') {
                    continue;
                }
                
                // 计算小方块的索引
                int boxIndex = (i / 3) * 3 + j / 3;
                
                // 检查当前数字是否已经在行、列或小方块中出现
                if (rows[i].contains(num) || cols[j].contains(num) || boxes[boxIndex].contains(num)) {
                    return false;
                }
                
                // 将当前数字添加到对应的哈希表中
                rows[i].add(num);
                cols[j].add(num);
                boxes[boxIndex].add(num);
            }
        }
        
        return true;
    }
    
    /**
     * LeetCode 454. 4Sum II (四数相加 II)
     * 题目来源: https://leetcode.com/problems/4sum-ii/
     * 
     * 题目描述:
     * 给你四个整数数组 nums1、nums2、nums3 和 nums4 ，数组长度都是 n ，请你计算有多少个元组 (i, j, k, l) 能满足：
     * 0 <= i, j, k, l < n
     * nums1[i] + nums2[j] + nums3[k] + nums4[l] == 0
     * 
     * 示例:
     * 输入: nums1 = [1,2], nums2 = [-2,-1], nums3 = [-1,2], nums4 = [0,2]
     * 输出: 2
     * 解释: 两个元组如下:
     * 1. (0, 0, 0, 1) -> nums1[0] + nums2[0] + nums3[0] + nums4[1] = 1 + (-2) + (-1) + 2 = 0
     * 2. (1, 1, 0, 0) -> nums1[1] + nums2[1] + nums3[0] + nums4[0] = 2 + (-1) + (-1) + 0 = 0
     * 
     * 算法思路:
     * 将四个数组分成两部分，计算前两个数组所有可能的和及其出现次数，
     * 然后计算后两个数组的所有可能的和，检查其相反数在前两个数组的和中出现的次数
     * 
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n^2)
     */
    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        // 使用哈希表存储前两个数组的和及其出现次数
        Map<Integer, Integer> sumCount = new HashMap<>();
        
        for (int num1 : nums1) {
            for (int num2 : nums2) {
                int sum = num1 + num2;
                sumCount.put(sum, sumCount.getOrDefault(sum, 0) + 1);
            }
        }
        
        int count = 0;
        
        for (int num3 : nums3) {
            for (int num4 : nums4) {
                int sum = num3 + num4;
                count += sumCount.getOrDefault(-sum, 0);
            }
        }
        
        return count;
    }
    
    /**
     * LeetCode 525. Contiguous Array (连续数组)
     * 题目来源: https://leetcode.com/problems/contiguous-array/
     * 
     * 题目描述:
     * 给定一个二进制数组 nums , 找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
     * 
     * 示例:
     * 输入: nums = [0,1]
     * 输出: 2
     * 解释: [0, 1] 是具有相同数量0和1的最长连续子数组。
     * 
     * 输入: nums = [0,1,0]
     * 输出: 2
     * 解释: [0, 1] (或 [1, 0]) 是具有相同数量0和1的最长连续子数组。
     * 
     * 算法思路:
     * 将0视为-1，1视为1，问题转化为求和为0的最长子数组
     * 使用哈希表记录前缀和及其首次出现的位置
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public int findMaxLength(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1); // 初始和为0，位置为-1
        int maxLength = 0;
        int count = 0;
        
        for (int i = 0; i < nums.length; i++) {
            // 将0视为-1，1视为1
            count += nums[i] == 0 ? -1 : 1;
            
            if (map.containsKey(count)) {
                maxLength = Math.max(maxLength, i - map.get(count));
            } else {
                map.put(count, i);
            }
        }
        
        return maxLength;
    }
    
    /**
     * LeetCode 692. Top K Frequent Words (前K个高频单词)
     * 题目来源: https://leetcode.com/problems/top-k-frequent-words/
     * 
     * 题目描述:
     * 给你一个单词数组 words 和一个整数 k ，请你返回前 k 个出现次数最多的单词。
     * 返回的答案应该按单词出现频率由高到低排序。如果不同的单词有相同出现频率，按字母顺序排序。
     * 
     * 示例:
     * 输入: words = ["i","love","leetcode","i","love","coding"], k = 2
     * 输出: ["i","love"]
     * 解释: "i" 和 "love" 是出现次数最多的两个单词，均为2次。
     * 注意，按字母顺序 "i" 在 "love" 之前。
     * 
     * 算法思路:
     * 使用哈希表统计每个单词的频率，然后使用优先队列按频率和字典序排序
     * 
     * 时间复杂度: O(n log k)
     * 空间复杂度: O(n)
     */
    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        
        PriorityQueue<String> pq = new PriorityQueue<>((a, b) -> {
            int freqCompare = wordCount.get(a) - wordCount.get(b);
            if (freqCompare != 0) {
                return freqCompare;
            }
            // 频率相同时，按字母顺序逆序排列（因为是小顶堆）
            return b.compareTo(a);
        });
        
        for (String word : wordCount.keySet()) {
            pq.offer(word);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        
        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }
        Collections.reverse(result);
        
        return result;
    }
}