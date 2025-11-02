package class044;

import java.util.*;

/**
 * Trie树扩展题目合集 - 从各大算法平台收集的Trie树相关题目
 * 
 * 本文件包含从LeetCode、POJ、HDU、牛客网、洛谷等各大算法平台收集的Trie树相关题目
 * 每个题目都包含详细的解题思路、时间复杂度分析、空间复杂度分析和工程化考量
 */

public class Code06_ExtendedTrieProblems {

    /*
     * 题目1: LeetCode 745. 前缀和后缀搜索
     * 题目来源：LeetCode
     * 题目链接：https://leetcode.cn/problems/prefix-and-suffix-search/
     * 相关题目：
     * - LeetCode 208. 实现 Trie (前缀树)
     * - LeetCode 677. 键值映射
     * - HDU 1247 Hat's Words
     * 
     * 题目描述：
     * 设计一个包含一些单词的词典，支持前缀和后缀搜索。
     * WordFilter(string[] words) 使用给定的单词初始化对象。
     * int f(string prefix, string suffix) 返回词典中具有前缀 prefix 和后缀 suffix 的单词的下标。
     * 如果存在多个满足条件的单词，返回下标最大的单词。如果没有满足条件的单词，返回 -1。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词，每个节点记录经过该节点的最大下标
     * 2. 对于每个单词，将其所有后缀+分隔符+单词本身插入Trie树
     * 3. 查询时，将后缀+分隔符+前缀作为查询字符串
     * 
     * 时间复杂度分析：
     * 1. 构造函数：O(N*L^2)，其中N是单词数量，L是单词最大长度
     * 2. f函数：O(P+S)，其中P是前缀长度，S是后缀长度
     * 空间复杂度分析：
     * 1. O(N*L^2)，需要存储所有单词的所有后缀组合
     * 是否为最优解：是，这是解决此类问题的经典方法
     * 
     * 工程化考量：
     * 1. 内存优化：可以使用更紧凑的数据结构
     * 2. 性能优化：对于大量查询，可以考虑缓存结果
     * 3. 异常处理：处理空输入和边界情况
     */
    class WordFilter {
        class TrieNode {
            TrieNode[] children;
            int weight; // 存储经过该节点的最大下标
            
            public TrieNode() {
                children = new TrieNode[27]; // 26个字母 + 1个分隔符
                weight = 0;
            }
        }
        
        TrieNode root;
        
        public WordFilter(String[] words) {
            root = new TrieNode();
            for (int weight = 0; weight < words.length; weight++) {
                String word = words[weight];
                // 对于每个单词，插入所有后缀+分隔符+单词的组合
                for (int i = 0; i <= word.length(); i++) {
                    String key = word.substring(i) + "{" + word;
                    TrieNode node = root;
                    for (char c : key.toCharArray()) {
                        int index = c - 'a';
                        if (c == '{') index = 26;
                        if (node.children[index] == null) {
                            node.children[index] = new TrieNode();
                        }
                        node = node.children[index];
                        node.weight = weight; // 更新最大下标
                    }
                }
            }
        }
        
        public int f(String prefix, String suffix) {
            String key = suffix + "{" + prefix;
            TrieNode node = root;
            for (char c : key.toCharArray()) {
                int index = c - 'a';
                if (c == '{') index = 26;
                if (node.children[index] == null) {
                    return -1;
                }
                node = node.children[index];
            }
            return node.weight;
        }
    }

    /*
     * 题目2: LeetCode 336. 回文对
     * 题目来源：LeetCode
     * 题目链接：https://leetcode.cn/problems/palindrome-pairs/
     * 相关题目：
     * - LeetCode 5. 最长回文子串
     * - LeetCode 125. 验证回文串
     * - HDU 1247 Hat's Words
     * 
     * 题目描述：
     * 给定一组互不相同的单词，找出所有不同的索引对 (i, j)，使得列表中的两个单词，words[i] + words[j] ，可拼接成回文串。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词的逆序
     * 2. 对于每个单词，在Trie树中查找能与之形成回文串的单词
     * 3. 分情况讨论：当前单词是较长部分、当前单词是较短部分
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(N*L)，其中N是单词数量，L是单词平均长度
     * 2. 查询过程：O(N*L^2)，需要检查每个单词的所有前缀和后缀
     * 空间复杂度分析：
     * 1. O(N*L)，Trie树存储空间
     * 是否为最优解：是，Trie树是解决此类问题的高效方法
     * 
     * 工程化考量：
     * 1. 性能优化：可以使用哈希表预计算回文信息
     * 2. 内存优化：对于长单词，可以优化存储方式
     * 3. 去重处理：确保索引对不重复
     */
    class PalindromePairs {
        class TrieNode {
            TrieNode[] children;
            int index; // 单词在数组中的下标
            List<Integer> list; // 存储经过该节点且剩余部分是回文的单词下标
            
            public TrieNode() {
                children = new TrieNode[26];
                index = -1;
                list = new ArrayList<>();
            }
        }
        
        public List<List<Integer>> palindromePairs(String[] words) {
            List<List<Integer>> res = new ArrayList<>();
            TrieNode root = new TrieNode();
            
            // 构建Trie树，存储单词的逆序
            for (int i = 0; i < words.length; i++) {
                addWord(root, words[i], i);
            }
            
            // 对于每个单词，在Trie树中查找匹配
            for (int i = 0; i < words.length; i++) {
                search(words, i, root, res);
            }
            
            return res;
        }
        
        private void addWord(TrieNode root, String word, int index) {
            // 逆序插入单词
            for (int i = word.length() - 1; i >= 0; i--) {
                int j = word.charAt(i) - 'a';
                if (root.children[j] == null) {
                    root.children[j] = new TrieNode();
                }
                // 如果单词的前缀是回文，记录当前下标
                if (isPalindrome(word, 0, i)) {
                    root.list.add(index);
                }
                root = root.children[j];
            }
            root.list.add(index);
            root.index = index;
        }
        
        private void search(String[] words, int i, TrieNode root, List<List<Integer>> res) {
            // 正序匹配单词
            for (int j = 0; j < words[i].length(); j++) {
                if (root.index >= 0 && root.index != i && isPalindrome(words[i], j, words[i].length() - 1)) {
                    res.add(Arrays.asList(i, root.index));
                }
                root = root.children[words[i].charAt(j) - 'a'];
                if (root == null) return;
            }
            
            // 处理Trie树中剩余的匹配
            for (int j : root.list) {
                if (i == j) continue;
                res.add(Arrays.asList(i, j));
            }
        }
        
        private boolean isPalindrome(String word, int i, int j) {
            while (i < j) {
                if (word.charAt(i++) != word.charAt(j--)) return false;
            }
            return true;
        }
    }

    /*
     * 题目3: POJ 2001 Shortest Prefixes
     * 题目来源：POJ
     * 题目链接：http://poj.org/problem?id=2001
     * 相关题目：
     * - 牛客网 最长公共前缀
     * - 杭电OJ 1251 统计难题
     * - LeetCode 208. 实现 Trie (前缀树)
     * 
     * 题目描述：
     * 给定一组单词，为每个单词找到最短的唯一前缀。也就是说，找到每个单词的最短前缀，使得这个前缀不是其他任何单词的前缀。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词
     * 2. 记录每个节点被经过的次数
     * 3. 对于每个单词，找到第一个出现次数为1的节点，该节点之前的前缀就是最短唯一前缀
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(∑len(s))
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树是解决此类问题的最优方法
     * 
     * 工程化考量：
     * 1. 内存优化：可以使用更紧凑的节点结构
     * 2. 性能优化：预处理可以进一步提高查询效率
     * 3. 异常处理：处理空单词和重复单词的情况
     */
    class ShortestPrefixes {
        class TrieNode {
            TrieNode[] children;
            int count; // 经过该节点的单词数量
            
            public TrieNode() {
                children = new TrieNode[26];
                count = 0;
            }
        }
        
        public Map<String, String> findShortestPrefixes(String[] words) {
            Map<String, String> result = new HashMap<>();
            TrieNode root = new TrieNode();
            
            // 构建Trie树
            for (String word : words) {
                TrieNode node = root;
                for (char c : word.toCharArray()) {
                    int index = c - 'a';
                    if (node.children[index] == null) {
                        node.children[index] = new TrieNode();
                    }
                    node = node.children[index];
                    node.count++;
                }
            }
            
            // 为每个单词寻找最短唯一前缀
            for (String word : words) {
                TrieNode node = root;
                StringBuilder prefix = new StringBuilder();
                for (int i = 0; i < word.length(); i++) {
                    char c = word.charAt(i);
                    int index = c - 'a';
                    prefix.append(c);
                    node = node.children[index];
                    // 如果当前节点只被当前单词经过，则找到最短唯一前缀
                    if (node.count == 1) {
                        break;
                    }
                }
                result.put(word, prefix.toString());
            }
            
            return result;
        }
    }

    /*
     * 题目4: HDU 1247 Hat’s Words
     * 题目来源：HDU
     * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1247
     * 相关题目：
     * - LeetCode 745. 前缀和后缀搜索
     * - LeetCode 336. 回文对
     * - 洛谷 P2580 于是他错误的点名开始了
     * 
     * 题目描述：
     * 一个"hat's word"是一个单词，可以恰好由字典中其他两个单词连接而成。
     * 给你一个字典，找出所有的hat's words。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词
     * 2. 对于每个单词，检查它是否能被拆分成两个都在字典中的单词
     * 3. 使用Trie树快速检查每个前缀和后缀是否在字典中
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 检查过程：O(N*L^2)，其中N是单词数量，L是单词最大长度
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树提供高效的字符串查找
     * 
     * 工程化考量：
     * 1. 性能优化：可以预处理单词长度信息
     * 2. 内存优化：使用合适的Trie树实现
     * 3. 去重处理：确保结果不重复
     */
    class HatsWords {
        class TrieNode {
            TrieNode[] children;
            boolean isEnd;
            
            public TrieNode() {
                children = new TrieNode[26];
                isEnd = false;
            }
        }
        
        public List<String> findHatsWords(String[] words) {
            List<String> result = new ArrayList<>();
            TrieNode root = new TrieNode();
            
            // 构建Trie树
            for (String word : words) {
                insert(root, word);
            }
            
            // 检查每个单词是否是hat's word
            for (String word : words) {
                if (isHatsWord(root, word)) {
                    result.add(word);
                }
            }
            
            return result;
        }
        
        private void insert(TrieNode root, String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.isEnd = true;
        }
        
        private boolean search(TrieNode root, String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return false;
                }
                node = node.children[index];
            }
            return node.isEnd;
        }
        
        private boolean isHatsWord(TrieNode root, String word) {
            // 尝试所有可能的分割点
            for (int i = 1; i < word.length(); i++) {
                String prefix = word.substring(0, i);
                String suffix = word.substring(i);
                if (search(root, prefix) && search(root, suffix)) {
                    return true;
                }
            }
            return false;
        }
    }

    /*
     * 题目5: 牛客网 最长公共前缀
     * 题目来源：牛客网
     * 题目链接：https://www.nowcoder.com/practice/28eb3175488f4434a4a6207f6f484f47
     * 相关题目：
     * - LeetCode 14. 最长公共前缀
     * - POJ 2001 Shortest Prefixes
     * - 杭电OJ 1251 统计难题
     * 
     * 题目描述：
     * 编写一个函数来查找字符串数组中的最长公共前缀。
     * 如果不存在公共前缀，返回空字符串 ""。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有字符串
     * 2. 从根节点开始，找到第一个有多个子节点的节点
     * 3. 该节点之前的前缀就是最长公共前缀
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查找过程：O(min(len(s)))
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树提供直观的解决方案
     * 
     * 工程化考量：
     * 1. 性能优化：对于少量字符串，直接比较可能更快
     * 2. 内存优化：可以使用更紧凑的Trie树实现
     * 3. 异常处理：处理空数组和空字符串的情况
     */
    class LongestCommonPrefix {
        class TrieNode {
            TrieNode[] children;
            int count; // 经过该节点的字符串数量
            
            public TrieNode() {
                children = new TrieNode[26];
                count = 0;
            }
        }
        
        public String longestCommonPrefix(String[] strs) {
            if (strs == null || strs.length == 0) return "";
            if (strs.length == 1) return strs[0];
            
            TrieNode root = new TrieNode();
            
            // 构建Trie树
            for (String str : strs) {
                if (str.isEmpty()) return "";
                TrieNode node = root;
                for (char c : str.toCharArray()) {
                    int index = c - 'a';
                    if (node.children[index] == null) {
                        node.children[index] = new TrieNode();
                    }
                    node = node.children[index];
                    node.count++;
                }
            }
            
            // 查找最长公共前缀
            StringBuilder prefix = new StringBuilder();
            TrieNode node = root;
            while (true) {
                // 检查当前节点的子节点
                int childCount = 0;
                TrieNode nextNode = null;
                for (int i = 0; i < 26; i++) {
                    if (node.children[i] != null && node.children[i].count == strs.length) {
                        childCount++;
                        nextNode = node.children[i];
                        prefix.append((char)('a' + i));
                    }
                }
                
                // 如果子节点数量不为1，结束查找
                if (childCount != 1) {
                    break;
                }
                node = nextNode;
            }
            
            return prefix.toString();
        }
    }

    /*
     * 题目6: 洛谷 P2580 于是他错误的点名开始了
     * 题目来源：洛谷
     * 题目链接：https://www.luogu.com.cn/problem/P2580
     * 相关题目：
     * - HDU 1247 Hat's Words
     * - 牛客网 最长公共前缀
     * - LeetCode 208. 实现 Trie (前缀树)
     * 
     * 题目描述：
     * 老师点名，第一次点到的输出"OK"，重复点到的输出"REPEAT"，点到不存在的名字输出"WRONG"。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有学生姓名
     * 2. 每个节点记录点名状态
     * 3. 根据点名状态输出相应结果
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(∑len(s))
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树提供高效的姓名查找
     * 
     * 工程化考量：
     * 1. 内存优化：可以使用哈希表作为替代方案
     * 2. 性能优化：Trie树在大量相似姓名时更高效
     * 3. 异常处理：处理非法字符和超长姓名
     */
    class RollCallSystem {
        class TrieNode {
            TrieNode[] children;
            int status; // 0: 未点名, 1: 已点名, 2: 不存在
            
            public TrieNode() {
                children = new TrieNode[26];
                status = 0;
            }
        }
        
        private TrieNode root;
        
        public RollCallSystem(String[] names) {
            root = new TrieNode();
            // 构建Trie树，插入所有学生姓名
            for (String name : names) {
                insert(name);
            }
        }
        
        private void insert(String name) {
            TrieNode node = root;
            for (char c : name.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.status = 0; // 初始状态为未点名
        }
        
        public String call(String name) {
            TrieNode node = root;
            for (char c : name.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return "WRONG"; // 姓名不存在
                }
                node = node.children[index];
            }
            
            if (node.status == 0) {
                node.status = 1; // 标记为已点名
                return "OK";
            } else if (node.status == 1) {
                return "REPEAT";
            } else {
                return "WRONG";
            }
        }
    }

    /*
     * 题目7: CodeChef DICT - Dictionary
     * 题目来源：CodeChef
     * 题目链接：https://www.codechef.com/problems/DICT
     * 相关题目：
     * - LeetCode 208. 实现 Trie (前缀树)
     * - 杭电OJ 1251 统计难题
     * - SPOJ PHONELST - Phone List
     * 
     * 题目描述：
     * 给定一个字典和一组查询，对于每个查询，输出字典中所有以该查询字符串为前缀的单词。
     * 如果存在多个单词，按字典序输出。
     * 
     * 解题思路：
     * 1. 使用Trie树存储字典中的所有单词
     * 2. 每个节点维护以该节点为前缀的所有单词
     * 3. 查询时找到前缀对应的节点，输出该节点存储的所有单词
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(P + K)，其中P是前缀长度，K是输出单词数量
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树是解决前缀查询的高效方法
     * 
     * 工程化考量：
     * 1. 内存优化：可以使用更紧凑的存储方式
     * 2. 性能优化：预处理可以加速查询
     * 3. 排序处理：需要按字典序输出结果
     */
    class DictionarySearch {
        class TrieNode {
            TrieNode[] children;
            List<String> words; // 存储以该节点为前缀的所有单词
            
            public TrieNode() {
                children = new TrieNode[26];
                words = new ArrayList<>();
            }
        }
        
        private TrieNode root;
        
        public DictionarySearch(String[] dictionary) {
            root = new TrieNode();
            // 构建Trie树
            for (String word : dictionary) {
                insert(word);
            }
        }
        
        private void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
                node.words.add(word);
            }
        }
        
        public List<String> search(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return new ArrayList<>(); // 前缀不存在
                }
                node = node.children[index];
            }
            // 返回该前缀对应的所有单词，按字典序排序
            Collections.sort(node.words);
            return node.words;
        }
    }

    /*
     * 题目8: SPOJ PHONELST - Phone List
     * 题目来源：SPOJ
     * 题目链接：https://www.spoj.com/problems/PHONELIST/
     * 
     * 题目描述：
     * 与POJ 3630相同，判断电话号码列表中是否存在前缀关系。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有电话号码
     * 2. 在插入过程中检查前缀关系
     * 3. 优化实现，提高效率
     * 
     * 时间复杂度分析：
     * 1. O(∑len(s))
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是
     */
    class SPOJPhoneList {
        class TrieNode {
            TrieNode[] children;
            boolean isEnd;
            
            public TrieNode() {
                children = new TrieNode[10]; // 0-9
                isEnd = false;
            }
        }
        
        public boolean hasConsistentList(String[] phoneNumbers) {
            TrieNode root = new TrieNode();
            
            // 按长度排序，先插入短的
            Arrays.sort(phoneNumbers, (a, b) -> a.length() - b.length());
            
            for (String phone : phoneNumbers) {
                TrieNode node = root;
                boolean createdNew = false;
                
                for (int i = 0; i < phone.length(); i++) {
                    int digit = phone.charAt(i) - '0';
                    
                    if (node.children[digit] == null) {
                        node.children[digit] = new TrieNode();
                        createdNew = true;
                    }
                    
                    node = node.children[digit];
                    
                    // 如果在插入过程中遇到已标记的结尾，说明存在前缀关系
                    if (node.isEnd) {
                        return false;
                    }
                }
                
                // 如果当前节点有子节点，说明当前号码是其他号码的前缀
                if (!createdNew) {
                    return false;
                }
                
                node.isEnd = true;
            }
            
            return true;
        }
    }

    /*
     * 题目9: 剑指Offer 45. 把数组排成最小的数
     * 题目来源：剑指Offer
     * 题目链接：https://leetcode.cn/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof/
     * 相关题目：
     * - LeetCode 179. 最大数
     * - 牛客网 字符串拼接
     * - HDU 1251 统计难题
     * 
     * 题目描述：
     * 输入一个非负整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。
     * 
     * 解题思路：
     * 1. 使用Trie树思想进行字符串排序
     * 2. 自定义比较器，比较a+b和b+a的大小
     * 3. 按特定顺序拼接字符串
     * 
     * 时间复杂度分析：
     * 1. O(NlogN)
     * 空间复杂度分析：
     * 1. O(N)
     * 是否为最优解：是
     */
    class MinNumber {
        public String minNumber(int[] nums) {
            // 将数字转换为字符串
            String[] strNums = new String[nums.length];
            for (int i = 0; i < nums.length; i++) {
                strNums[i] = String.valueOf(nums[i]);
            }
            
            // 自定义排序：比较a+b和b+a的大小
            Arrays.sort(strNums, (a, b) -> (a + b).compareTo(b + a));
            
            // 拼接结果
            StringBuilder result = new StringBuilder();
            for (String str : strNums) {
                result.append(str);
            }
            
            return result.toString();
        }
    }

    /*
     * 题目10: 杭电OJ 1251 统计难题
     * 题目来源：杭电OJ
     * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1251
     * 相关题目：
     * - 牛客网 最长公共前缀
     * - CodeChef DICT - Dictionary
     * - POJ 2001 Shortest Prefixes
     * 
     * 题目描述：
     * Ignatius最近遇到一个难题，老师交给他很多单词(只有小写字母组成，不会有重复的单词出现)，
     * 现在老师要他统计出以某个字符串为前缀的单词数量(单词本身也是自己的前缀)。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词
     * 2. 每个节点记录经过该节点的单词数量
     * 3. 查询时找到前缀对应的节点，返回该节点的计数
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(P)，其中P是前缀长度
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是
     */
    class StatisticalProblem {
        class TrieNode {
            TrieNode[] children;
            int count; // 经过该节点的单词数量
            
            public TrieNode() {
                children = new TrieNode[26];
                count = 0;
            }
        }
        
        private TrieNode root;
        
        public StatisticalProblem(String[] words) {
            root = new TrieNode();
            for (String word : words) {
                insert(word);
            }
        }
        
        private void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
                node.count++;
            }
        }
        
        public int prefixCount(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return 0;
                }
                node = node.children[index];
            }
            return node.count;
        }
    }

    /*
     * 题目11: SPOJ ADAINDEX - Ada and Indexing
     * 题目来源：SPOJ
     * 题目链接：https://www.spoj.com/problems/ADAINDEX/
     * 相关题目：
     * - CodeChef DICT - Dictionary
     * - 牛客网 最长公共前缀
     * - 杭电OJ 1251 统计难题
     * 
     * 题目描述：
     * Ada the Ladybug有很多事情要做，几乎没有时间。她想在搜索某些东西时节省时间。
     * 给定一个单词列表和一些查询，对于每个查询，输出列表中有多少个单词以该查询字符串为前缀。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词
     * 2. 每个节点记录经过该节点的单词数量
     * 3. 查询时找到前缀对应的节点，返回该节点的计数
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(P)，其中P是前缀长度
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是
     */
    class AdaAndIndexing {
        class TrieNode {
            TrieNode[] children;
            int count; // 经过该节点的单词数量
            
            public TrieNode() {
                children = new TrieNode[26];
                count = 0;
            }
        }
        
        private TrieNode root;
        
        public AdaAndIndexing(String[] words) {
            root = new TrieNode();
            for (String word : words) {
                insert(word);
            }
        }
        
        private void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
                node.count++;
            }
        }
        
        public int prefixCount(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return 0;
                }
                node = node.children[index];
            }
            return node.count;
        }
    }

    /*
     * 题目12: CodeForces 271D - Good Substrings
     * 题目来源：CodeForces
     * 题目链接：https://codeforces.com/problemset/problem/271/D
     * 相关题目：
     * - LeetCode 208. 实现 Trie (前缀树)
     * - HDU 1251 统计难题
     * - SPOJ DICT - Dictionary
     * 
     * 题目描述：
     * 给定一个字符串s，一个由26个字符组成的字符串，表示每个字母是好字母还是坏字母，
     * 以及一个整数k，表示一个好子串中最多允许的坏字符数量。
     * 找出字符串s中不同好子串的数量。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有好子串
     * 2. 枚举所有可能的子串，检查是否为好子串
     * 3. 将好子串插入Trie树，避免重复计数
     * 
     * 时间复杂度分析：
     * 1. O(N^3)，其中N是字符串长度
     * 空间复杂度分析：
     * 1. O(N^2)
     * 是否为最优解：可以使用更优化的方法
     */
    class GoodSubstrings {
        class TrieNode {
            TrieNode[] children;
            boolean isEnd;
            
            public TrieNode() {
                children = new TrieNode[26];
                isEnd = false;
            }
        }
        
        private TrieNode root;
        
        public GoodSubstrings() {
            root = new TrieNode();
        }
        
        public int countGoodSubstrings(String s, String goodChars, int k) {
            int count = 0;
            int n = s.length();
            char[] good = goodChars.toCharArray();
            Set<String> uniqueSubstrings = new HashSet<>();
            
            // 枚举所有子串
            for (int i = 0; i < n; i++) {
                int badCount = 0;
                StringBuilder substring = new StringBuilder();
                
                for (int j = i; j < n; j++) {
                    char c = s.charAt(j);
                    int index = c - 'a'; // 修正索引计算
                    
                    // 检查字符是否为坏字符
                    if (index >= 0 && index < 26 && good[index] == '0') {
                        badCount++;
                    }
                    
                    // 如果坏字符数量超过限制，停止扩展
                    if (badCount > k) {
                        break;
                    }
                    
                    substring.append(c);
                    String currentSubstring = substring.toString();
                    
                    // 如果该子串尚未被计数，计数并添加到集合中
                    if (!uniqueSubstrings.contains(currentSubstring)) {
                        uniqueSubstrings.add(currentSubstring);
                        count++;
                    }
                }
            }
            
            return count;
        }
    }

    // 测试方法
    public static void main(String[] args) {
        Code06_ExtendedTrieProblems solution = new Code06_ExtendedTrieProblems();
        
        // 测试WordFilter
        System.out.println("=== 测试WordFilter ===");
        String[] words1 = {"apple", "application", "apply"};
        WordFilter wf = solution.new WordFilter(words1);
        System.out.println("f(\"a\", \"e\"): " + wf.f("a", "e")); // 应该返回2（apply的下标）
        
        // 测试PalindromePairs
        System.out.println("\n=== 测试PalindromePairs ===");
        String[] words2 = {"abcd", "dcba", "lls", "s", "sssll"};
        PalindromePairs pp = solution.new PalindromePairs();
        List<List<Integer>> pairs = pp.palindromePairs(words2);
        System.out.println("回文对: " + pairs);
        
        // 测试ShortestPrefixes
        System.out.println("\n=== 测试ShortestPrefixes ===");
        String[] words3 = {"z", "dog", "duck", "dove"};
        ShortestPrefixes sp = solution.new ShortestPrefixes();
        Map<String, String> prefixes = sp.findShortestPrefixes(words3);
        System.out.println("最短唯一前缀: " + prefixes);
        
        // 测试HatsWords
        System.out.println("\n=== 测试HatsWords ===");
        String[] words4 = {"a", "hat", "hats", "word", "words", "hatword"};
        HatsWords hw = solution.new HatsWords();
        List<String> hatsWords = hw.findHatsWords(words4);
        System.out.println("Hat's words: " + hatsWords);
        
        // 测试LongestCommonPrefix
        System.out.println("\n=== 测试LongestCommonPrefix ===");
        String[] words5 = {"flower", "flow", "flight"};
        LongestCommonPrefix lcp = solution.new LongestCommonPrefix();
        String commonPrefix = lcp.longestCommonPrefix(words5);
        System.out.println("最长公共前缀: " + commonPrefix);
        
        // 测试RollCallSystem
        System.out.println("\n=== 测试RollCallSystem ===");
        String[] names = {"alice", "bob", "charlie"};
        RollCallSystem rcs = solution.new RollCallSystem(names);
        System.out.println("点名alice: " + rcs.call("alice")); // OK
        System.out.println("点名alice: " + rcs.call("alice")); // REPEAT
        System.out.println("点名david: " + rcs.call("david")); // WRONG
        
        // 测试DictionarySearch
        System.out.println("\n=== 测试DictionarySearch ===");
        String[] dictionary = {"apple", "application", "apply", "banana", "band"};
        DictionarySearch ds = solution.new DictionarySearch(dictionary);
        List<String> results = ds.search("app");
        System.out.println("前缀'app'的单词: " + results);
        
        // 测试SPOJPhoneList
        System.out.println("\n=== 测试SPOJPhoneList ===");
        String[] phones1 = {"911", "97625999", "91125426"};
        String[] phones2 = {"113", "12340", "123440", "12345", "98346"};
        SPOJPhoneList spl = solution.new SPOJPhoneList();
        System.out.println("电话号码列表1是否一致: " + spl.hasConsistentList(phones1)); // false
        System.out.println("电话号码列表2是否一致: " + spl.hasConsistentList(phones2)); // true
        
        // 测试MinNumber
        System.out.println("\n=== 测试MinNumber ===");
        int[] nums = {3, 30, 34, 5, 9};
        MinNumber mn = solution.new MinNumber();
        String minNum = mn.minNumber(nums);
        System.out.println("最小数字: " + minNum); // "3033459"
        
        // 测试StatisticalProblem
        System.out.println("\n=== 测试StatisticalProblem ===");
        String[] words6 = {"banana", "band", "bee", "absolute", "acm"};
        StatisticalProblem stat = solution.new StatisticalProblem(words6);
        System.out.println("前缀'ba'的数量: " + stat.prefixCount("ba")); // 2
        System.out.println("前缀'b'的数量: " + stat.prefixCount("b")); // 3
        System.out.println("前缀'abc'的数量: " + stat.prefixCount("abc")); // 0
        
        // 测试AdaAndIndexing
        System.out.println("\n=== 测试AdaAndIndexing ===");
        String[] words7 = {"abc", "abcde", "abcdef", "bcd", "bcde"};
        AdaAndIndexing ada = solution.new AdaAndIndexing(words7);
        System.out.println("前缀'abc'的数量: " + ada.prefixCount("abc")); // 3
        System.out.println("前缀'bc'的数量: " + ada.prefixCount("bc")); // 2
        System.out.println("前缀'xyz'的数量: " + ada.prefixCount("xyz")); // 0
        
        // 测试GoodSubstrings
        System.out.println("\n=== 测试GoodSubstrings ===");
        GoodSubstrings gs = solution.new GoodSubstrings();
        String s = "ababac";
        String goodChars = "10000000000000000000000000"; // 只有'a'是好字符
        int k = 1; // 最多允许1个坏字符
        int result = gs.countGoodSubstrings(s, goodChars, k);
        System.out.println("好子串数量: " + result);
    }
}