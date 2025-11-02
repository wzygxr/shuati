import java.util.*;

/**
 * Comparator高级应用题目实现
 * 包含LeetCode 524, 937, 1331, 1366, 1451, 1509, 1561, 1636, 1710, 1859等题目
 * 
 * Comparator高级特性：
 * - 多级排序：按多个条件依次排序
 * - 稳定排序：保持相同元素的相对顺序
 * - 自定义排序逻辑：实现复杂的排序规则
 * - Lambda表达式：简化比较器实现
 * 
 * 时间复杂度分析：
 * - 排序操作：O(n log n)
 * - 自定义比较器：O(1)每次比较
 * - 复杂比较逻辑：O(k)每次比较，k为比较元素的复杂度
 * 
 * 空间复杂度分析：
 * - 排序算法：O(log n) 栈空间
 * - 额外数据结构：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理空输入、边界条件
 * 2. 性能优化：避免在比较器中创建新对象，使用缓存
 * 3. 代码可读性：清晰的比较逻辑和注释
 * 4. 稳定性：注意排序算法的稳定性要求
 * 
 * 相关平台题目：
 * 1. LeetCode 524. Longest Word in Dictionary through Deleting (通过删除字母匹配到字典里最长单词) - https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
 * 2. LeetCode 937. Reorder Data in Log Files (重新排列日志文件) - https://leetcode.com/problems/reorder-data-in-log-files/
 * 3. LeetCode 1331. Rank Transform of an Array (数组序号转换) - https://leetcode.com/problems/rank-transform-of-an-array/
 * 4. LeetCode 1366. Rank Teams by Votes (通过投票对团队排名) - https://leetcode.com/problems/rank-teams-by-votes/
 * 5. LeetCode 1451. Rearrange Words in a Sentence (重新排列句子中的单词) - https://leetcode.com/problems/rearrange-words-in-a-sentence/
 * 6. LeetCode 1509. Minimum Difference Between Largest and Smallest Value in Three Moves (三次操作后最大值与最小值的最小差) - https://leetcode.com/problems/minimum-difference-between-largest-and-smallest-value-in-three-moves/
 * 7. LeetCode 1561. Maximum Number of Coins You Can Get (你可以获得的最大硬币数目) - https://leetcode.com/problems/maximum-number-of-coins-you-can-get/
 * 8. LeetCode 1636. Sort Array by Increasing Frequency (按照频率将数组升序排序) - https://leetcode.com/problems/sort-array-by-increasing-frequency/
 * 9. LeetCode 1710. Maximum Units on a Truck (卡车上的最大单元数) - https://leetcode.com/problems/maximum-units-on-a-truck/
 * 10. LeetCode 1859. Sorting the Sentence (将句子排序) - https://leetcode.com/problems/sorting-the-sentence/
 * 11. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
 * 12. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
 * 13. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
 * 14. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
 * 15. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
 * 16. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
 * 17. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
 * 18. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
 * 19. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
 * 20. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
 * 21. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
 * 22. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
 * 23. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
 * 24. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
 * 25. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
 * 26. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
 * 27. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
 * 28. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
 * 29. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
 * 30. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
 * 31. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
 * 32. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
 * 33. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
 * 34. LeetCode 149. Max Points on a Line (直线上最多的点数) - https://leetcode.com/problems/max-points-on-a-line/
 * 35. LeetCode 215. Kth Largest Element in an Array (数组中的第K个最大元素) - https://leetcode.com/problems/kth-largest-element-in-an-array/
 * 36. LeetCode 295. Find Median from Data Stream (数据流的中位数) - https://leetcode.com/problems/find-median-from-data-stream/
 * 37. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 38. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
 * 39. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
 * 40. LeetCode 148. Sort List (排序链表) - https://leetcode.com/problems/sort-list/
 * 41. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
 * 42. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
 * 43. LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序) - https://leetcode.com/problems/sort-characters-by-frequency/
 * 44. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
 * 45. LeetCode 539. Minimum Time Difference (最小时间差) - https://leetcode.com/problems/minimum-time-difference/
 * 46. LeetCode 791. Custom Sort String (自定义字符串排序) - https://leetcode.com/problems/custom-sort-string/
 * 47. LeetCode 1030. Matrix Cells in Distance Order (距离顺序排列矩阵单元格) - https://leetcode.com/problems/matrix-cells-in-distance-order/
 * 48. LeetCode 1122. Relative Sort Array (数组的相对排序) - https://leetcode.com/problems/relative-sort-array/
 * 49. LeetCode 1356. Sort Integers by The Number of 1 Bits (根据数字二进制下 1 的数目排序) - https://leetcode.com/problems/sort-integers-by-the-number-of-1-bits/
 * 50. LeetCode 179. Largest Number (最大数) - https://leetcode.com/problems/largest-number/

public class Code05_ComparatorAdvanced {
    
    /**
     * LeetCode 524. Longest Word in Dictionary through Deleting
     * 通过删除字母匹配到字典里最长单词
     * 网址：https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
     * 
     * 解题思路：
     * 1. 使用自定义Comparator按长度降序、字典序升序排序
     * 2. 检查每个单词是否可以通过删除s中的字符得到
     * 3. 返回第一个满足条件的单词
     * 
     * 时间复杂度：O(n * x * log n)，其中x是单词平均长度
     * 空间复杂度：O(log n) 排序栈空间
     */
    public String findLongestWord(String s, List<String> dictionary) {
        // 自定义比较器：先按长度降序，再按字典序升序
        Collections.sort(dictionary, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                if (a.length() != b.length()) {
                    return Integer.compare(b.length(), a.length()); // 长度降序
                }
                return a.compareTo(b); // 字典序升序
            }
        });
        
        for (String word : dictionary) {
            if (isSubsequence(word, s)) {
                return word;
            }
        }
        return "";
    }
    
    private boolean isSubsequence(String word, String s) {
        int i = 0, j = 0;
        while (i < word.length() && j < s.length()) {
            if (word.charAt(i) == s.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == word.length();
    }
    
    /**
     * LeetCode 937. Reorder Data in Log Files
     * 重新排列日志文件
     * 网址：https://leetcode.com/problems/reorder-data-in-log-files/
     * 
     * 解题思路：
     * 1. 区分字母日志和数字日志
     * 2. 字母日志按内容字典序排序，内容相同按标识符排序
     * 3. 数字日志保持原有顺序
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public String[] reorderLogFiles(String[] logs) {
        Arrays.sort(logs, new Comparator<String>() {
            @Override
            public int compare(String log1, String log2) {
                // 分割标识符和内容
                String[] split1 = log1.split(" ", 2);
                String[] split2 = log2.split(" ", 2);
                
                boolean isDigit1 = Character.isDigit(split1[1].charAt(0));
                boolean isDigit2 = Character.isDigit(split2[1].charAt(0));
                
                // 情况1：都是字母日志
                if (!isDigit1 && !isDigit2) {
                    int cmp = split1[1].compareTo(split2[1]);
                    if (cmp != 0) {
                        return cmp;
                    }
                    // 内容相同，按标识符排序
                    return split1[0].compareTo(split2[0]);
                }
                
                // 情况2：一个是字母日志，一个是数字日志
                if (!isDigit1 && isDigit2) {
                    return -1; // 字母日志在前
                } else if (isDigit1 && !isDigit2) {
                    return 1;  // 数字日志在后
                } else {
                    // 情况3：都是数字日志，保持原有顺序
                    return 0;
                }
            }
        });
        
        return logs;
    }
    
    /**
     * LeetCode 1331. Rank Transform of an Array
     * 数组序号转换
     * 网址：https://leetcode.com/problems/rank-transform-of-an-array/
     * 
     * 解题思路：
     * 1. 创建索引数组并排序
     * 2. 使用自定义Comparator按元素值排序
     * 3. 分配排名，相同值排名相同
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public int[] arrayRankTransform(int[] arr) {
        int n = arr.length;
        if (n == 0) return new int[0];
        
        // 创建索引数组
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // 按元素值排序索引
        Arrays.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
                return Integer.compare(arr[i], arr[j]);
            }
        });
        
        int[] result = new int[n];
        int rank = 1;
        result[indices[0]] = rank;
        
        // 分配排名
        for (int i = 1; i < n; i++) {
            if (arr[indices[i]] != arr[indices[i-1]]) {
                rank++;
            }
            result[indices[i]] = rank;
        }
        
        return result;
    }
    
    /**
     * LeetCode 1366. Rank Teams by Votes
     * 通过投票对团队排名
     * 网址：https://leetcode.com/problems/rank-teams-by-votes/
     * 
     * 解题思路：
     * 1. 统计每个团队在每个位置的得票数
     * 2. 使用自定义Comparator比较投票统计
     * 3. 按得票规则排序团队
     * 
     * 时间复杂度：O(n * m + n log n)，其中m是投票位置数
     * 空间复杂度：O(n²)
     */
    public String rankTeams(String[] votes) {
        if (votes.length == 0) return "";
        if (votes.length == 1) return votes[0];
        
        int n = votes[0].length();
        // 统计每个团队在每个位置的得票数
        Map<Character, int[]> voteCount = new HashMap<>();
        for (String vote : votes) {
            for (int i = 0; i < n; i++) {
                char team = vote.charAt(i);
                voteCount.putIfAbsent(team, new int[n]);
                voteCount.get(team)[i]++;
            }
        }
        
        // 创建团队列表
        List<Character> teams = new ArrayList<>(voteCount.keySet());
        
        // 自定义比较器
        Collections.sort(teams, new Comparator<Character>() {
            @Override
            public int compare(Character a, Character b) {
                int[] votesA = voteCount.get(a);
                int[] votesB = voteCount.get(b);
                
                for (int i = 0; i < n; i++) {
                    if (votesA[i] != votesB[i]) {
                        return Integer.compare(votesB[i], votesA[i]); // 得票数降序
                    }
                }
                return Character.compare(a, b); // 字母序升序
            }
        });
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        for (char team : teams) {
            result.append(team);
        }
        return result.toString();
    }
    
    /**
     * LeetCode 1451. Rearrange Words in a Sentence
     * 重新排列句子中的单词
     * 网址：https://leetcode.com/problems/rearrange-words-in-a-text/
     * 
     * 解题思路：
     * 1. 按单词长度排序
     * 2. 长度相同保持原有顺序（稳定排序）
     * 3. 首字母大写处理
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public String arrangeWords(String text) {
        if (text == null || text.isEmpty()) return text;
        
        // 分割单词
        String[] words = text.toLowerCase().split(" ");
        
        // 创建索引数组
        Integer[] indices = new Integer[words.length];
        for (int i = 0; i < words.length; i++) {
            indices[i] = i;
        }
        
        // 按单词长度排序，保持稳定性
        Arrays.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
                return Integer.compare(words[i].length(), words[j].length());
            }
        });
        
        // 构建结果
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < indices.length; i++) {
            if (i == 0) {
                // 首字母大写
                String word = words[indices[i]];
                if (!word.isEmpty()) {
                    result.append(Character.toUpperCase(word.charAt(0)));
                    result.append(word.substring(1));
                }
            } else {
                result.append(words[indices[i]]);
            }
            if (i < indices.length - 1) {
                result.append(" ");
            }
        }
        
        return result.toString();
    }
    
    /**
     * LeetCode 1509. Minimum Difference Between Largest and Smallest Value in Three Moves
     * 三次操作后最大值与最小值的最小差
     * 网址：https://leetcode.com/problems/minimum-difference-between-largest-and-smallest-value-in-three-moves/
     * 
     * 解题思路：
     * 1. 排序数组
     * 2. 分析移除三个元素后的可能情况
     * 3. 计算最小差值
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     */
    public int minDifference(int[] nums) {
        int n = nums.length;
        if (n <= 4) return 0;
        
        Arrays.sort(nums);
        
        // 四种可能的情况：
        // 1. 移除最小的3个元素
        // 2. 移除最小的2个和最大的1个
        // 3. 移除最小的1个和最大的2个
        // 4. 移除最大的3个元素
        
        int minDiff = Integer.MAX_VALUE;
        minDiff = Math.min(minDiff, nums[n-1] - nums[3]);     // 移除最小的3个
        minDiff = Math.min(minDiff, nums[n-2] - nums[2]);     // 移除最小的2个和最大的1个
        minDiff = Math.min(minDiff, nums[n-3] - nums[1]);     // 移除最小的1个和最大的2个
        minDiff = Math.min(minDiff, nums[n-4] - nums[0]);     // 移除最大的3个
        
        return minDiff;
    }
    
    /**
     * LeetCode 1561. Maximum Number of Coins You Can Get
     * 你可以获得的最大硬币数目
     * 网址：https://leetcode.com/problems/maximum-number-of-coins-you-can-get/
     * 
     * 解题思路：
     * 1. 排序数组
     * 2. 每次取第二大的堆（贪心策略）
     * 3. 计算你能获得的总硬币数
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     */
    public int maxCoins(int[] piles) {
        Arrays.sort(piles);
        int n = piles.length;
        int result = 0;
        
        // 每次取第二大的堆
        for (int i = n - 2; i >= n / 3; i -= 2) {
            result += piles[i];
        }
        
        return result;
    }
    
    /**
     * LeetCode 1636. Sort Array by Increasing Frequency
     * 按照频率将数组升序排序
     * 网址：https://leetcode.com/problems/sort-array-by-increasing-frequency/
     * 
     * 解题思路：
     * 1. 统计每个数字的频率
     * 2. 使用自定义Comparator按频率升序排序
     * 3. 频率相同按数值降序排序
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public int[] frequencySort(int[] nums) {
        // 统计频率
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        
        // 转换为Integer数组用于排序
        Integer[] numsObj = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            numsObj[i] = nums[i];
        }
        
        // 自定义比较器
        Arrays.sort(numsObj, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                int freqA = freq.get(a);
                int freqB = freq.get(b);
                if (freqA != freqB) {
                    return Integer.compare(freqA, freqB); // 频率升序
                }
                return Integer.compare(b, a); // 数值降序
            }
        });
        
        // 转换回int数组
        for (int i = 0; i < nums.length; i++) {
            nums[i] = numsObj[i];
        }
        
        return nums;
    }
    
    /**
     * LeetCode 1710. Maximum Units on a Truck
     * 卡车上的最大单元数
     * 网址：https://leetcode.com/problems/maximum-units-on-a-truck/
     * 
     * 解题思路：
     * 1. 按单位单元数（每个箱子的单元数）降序排序
     * 2. 贪心选择单位单元数最大的箱子
     * 3. 计算最大单元数
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     */
    public int maximumUnits(int[][] boxTypes, int truckSize) {
        // 按单位单元数降序排序
        Arrays.sort(boxTypes, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[1], a[1]); // 单位单元数降序
            }
        });
        
        int result = 0;
        int remaining = truckSize;
        
        for (int[] box : boxTypes) {
            if (remaining <= 0) break;
            
            int boxesToTake = Math.min(remaining, box[0]);
            result += boxesToTake * box[1];
            remaining -= boxesToTake;
        }
        
        return result;
    }
    
    /**
     * LeetCode 1859. Sorting the Sentence
     * 将句子排序
     * 网址：https://leetcode.com/problems/sorting-the-sentence/
     * 
     * 解题思路：
     * 1. 提取单词末尾的数字
     * 2. 按数字排序单词
     * 3. 重新组合成句子
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public String sortSentence(String s) {
        String[] words = s.split(" ");
        
        // 自定义比较器：按末尾数字排序
        Arrays.sort(words, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                int numA = Integer.parseInt(a.substring(a.length() - 1));
                int numB = Integer.parseInt(b.substring(b.length() - 1));
                return Integer.compare(numA, numB);
            }
        });
        
        // 构建结果句子，去除末尾数字
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            result.append(words[i].substring(0, words[i].length() - 1));
            if (i < words.length - 1) {
                result.append(" ");
            }
        }
        
        return result.toString();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code05_ComparatorAdvanced solution = new Code05_ComparatorAdvanced();
        
        // 测试LeetCode 524
        String s = "abpcplea";
        List<String> dictionary = Arrays.asList("ale", "apple", "monkey", "plea");
        String longestWord = solution.findLongestWord(s, dictionary);
        System.out.println("LeetCode 524 Result: " + longestWord);
        
        // 测试LeetCode 937
        String[] logs = {"dig1 8 1 5 1", "let1 art can", "dig2 3 6", "let2 own kit dig", "let3 art zero"};
        String[] reorderedLogs = solution.reorderLogFiles(logs);
        System.out.println("LeetCode 937 Result: " + Arrays.toString(reorderedLogs));
        
        // 测试LeetCode 1331
        int[] arr = {40, 10, 20, 30};
        int[] ranks = solution.arrayRankTransform(arr);
        System.out.println("LeetCode 1331 Result: " + Arrays.toString(ranks));
        
        // 测试LeetCode 1710
        int[][] boxTypes = {{1,3}, {2,2}, {3,1}};
        int maxUnits = solution.maximumUnits(boxTypes, 4);
        System.out.println("LeetCode 1710 Result: " + maxUnits);
    }
}