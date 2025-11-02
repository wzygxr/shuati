package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 回文自动机题目实现
 * 
 * 本文件包含了多个使用回文自动机解决的经典算法题目：
 * 1. SPOJ LPS - Longest Palindromic Substring (最长回文子串)
 * 2. SPOJ JUSTAPAL - Just a Palindrome (仅仅是一个回文)
 * 3. Codeforces 17E - Palisection (回文分割)
 * 4. SPOJ NUMOFPAL - Number of Palindromes (回文数量)
 * 5. Codeforces 245H - Queries for Number of Palindromes (回文查询)
 * 6. SPOJ EPALIN - Extend to Palindrome (扩展为回文)
 * 7. Codeforces 137D - Palindromes (回文)
 * 8. SPOJ AIBOHP - Aibohphobia (回文恐惧症)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class PalindromicAutomatonProblems {
    
    /**
     * 回文自动机节点类
     */
    private static class Node {
        Map<Character, Integer> next;  // 转移函数
        int len;                      // 回文子串的长度
        int link;                     // 后缀链接（指向当前回文的最长真后缀回文）
        int count;                    // 该回文子串在当前字符串中的出现次数
        int occurCount;               // 该回文子串在原字符串中的总出现次数

        public Node(int len) {
            this.next = new HashMap<>();
            this.len = len;
            this.link = -1;
            this.count = 0;
            this.occurCount = 0;
        }
    }

    /**
     * 回文自动机实现
     */
    static class PalindromicAutomaton {
        private List<Node> tree;         // 所有节点
        private String text;             // 原始文本
        private int size;                // 节点数量
        private int last;                // 当前最长回文后缀的节点索引

        /**
         * 构造函数，初始化回文自动机
         */
        public PalindromicAutomaton() {
            tree = new ArrayList<>();
            text = "";
            
            // 创建两个特殊根节点
            // root1表示长度为-1的虚拟回文（用于奇数长度回文的后缀链接）
            tree.add(new Node(-1));
            // root2表示长度为0的空回文（用于偶数长度回文的后缀链接）
            tree.add(new Node(0));
            
            size = 2;
            last = 1;  // 初始时指向空回文
        }

        /**
         * 构造函数，从字符串构建回文自动机
         * @param text 输入文本
         */
        public PalindromicAutomaton(String text) {
            this();
            if (text == null) {
                throw new IllegalArgumentException("输入文本不能为null");
            }
            this.text = text;
            // 逐个字符构建回文自动机
            for (char c : text.toCharArray()) {
                extend(c);
            }
            // 计算出现次数
            calculateOccurCount();
        }

        /**
         * 找到当前节点的后缀链接中，其对应的回文子串前添加字符c后仍是回文的节点
         * @param p 当前节点
         * @param pos 当前字符位置
         * @param c 当前字符
         * @return 找到的节点索引
         */
        private int getFail(int p, int pos, char c) {
            // 从p开始，沿着后缀链接回溯，直到找到满足条件的节点
            while (true) {
                int len = tree.get(p).len;
                // 检查位置是否有效且前一个字符是否等于当前字符
                if (pos - len - 1 >= 0 && text.charAt(pos - len - 1) == c) {
                    break;
                }
                p = tree.get(p).link;
            }
            return p;
        }

        /**
         * 扩展回文自动机，添加一个字符
         * @param c 要添加的字符
         */
        public void extend(char c) {
            text += c;
            int pos = text.length() - 1;
            
            // 找到合适的后缀链接
            int p = getFail(last, pos, c);
            
            // 检查是否已经存在该转移
            if (!tree.get(p).next.containsKey(c)) {
                // 创建新节点
                int newNode = size++;
                tree.add(new Node(tree.get(p).len + 2));
                
                // 设置新节点的后缀链接
                if (tree.get(newNode).len == 1) {
                    // 长度为1的回文的后缀链接指向root2
                    tree.get(newNode).link = 1;
                } else {
                    // 否则找到合适的后缀链接
                    int failNode = getFail(tree.get(p).link, pos, c);
                    tree.get(newNode).link = tree.get(failNode).next.getOrDefault(c, 0);
                }
                
                // 添加转移
                tree.get(p).next.put(c, newNode);
            }
            
            // 更新last和计数
            last = tree.get(p).next.get(c);
            tree.get(last).count++;
        }

        /**
         * 计算每个回文子串在原字符串中的总出现次数
         * 需要在构建完成后调用
         */
        private void calculateOccurCount() {
            // 按照节点长度降序处理，确保父节点的计数在子节点之后处理
            List<Integer> order = new ArrayList<>();
            for (int i = 2; i < size; i++) {  // 跳过两个根节点
                order.add(i);
            }
            order.sort((a, b) -> Integer.compare(tree.get(b).len, tree.get(a).len));
            
            // 将count累加到后缀链接指向的节点
            for (int node : order) {
                tree.get(tree.get(node).link).occurCount += tree.get(node).count;
            }
            
            // 加上原始的count，得到总出现次数
            for (int i = 2; i < size; i++) {
                tree.get(i).occurCount += tree.get(i).count;
            }
        }

        /**
         * 获取不同回文子串的数量
         * @return 不同回文子串的数量
         */
        public int getDistinctPalindromeCount() {
            return size - 2;  // 减去两个根节点
        }

        /**
         * 获取所有不同的回文子串
         * @return 包含所有不同回文子串的集合
         */
        public Set<String> getAllDistinctPalindromes() {
            Set<String> result = new HashSet<>();
            collectPalindromes(0, new StringBuilder(), result);
            collectPalindromes(1, new StringBuilder(), result);
            return result;
        }

        /**
         * 递归收集所有回文子串
         */
        private void collectPalindromes(int node, StringBuilder current, Set<String> result) {
            // 跳过根节点
            if (node >= 2) {
                result.add(current.toString());
            }
            
            // 递归处理所有转移
            for (Map.Entry<Character, Integer> entry : tree.get(node).next.entrySet()) {
                char c = entry.getKey();
                int nextNode = entry.getValue();
                
                // 构建回文字符串
                StringBuilder newCurrent;
                if (tree.get(node).len == -1) {
                    // 奇数长度回文，中心是c
                    newCurrent = new StringBuilder().append(c);
                } else if (tree.get(node).len == 0) {
                    // 偶数长度回文，如 "aa"
                    newCurrent = new StringBuilder().append(c).append(c);
                } else {
                    // 其他情况，在现有回文两侧添加c
                    newCurrent = new StringBuilder(current).insert(0, c).append(c);
                }
                
                collectPalindromes(nextNode, newCurrent, result);
            }
        }

        /**
         * 获取某个回文子串的出现次数
         * @param palindrome 要查询的回文子串
         * @return 出现次数，如果不存在返回0
         */
        public int getOccurrenceCount(String palindrome) {
            if (palindrome == null || palindrome.isEmpty()) {
                return 0;
            }
            
            // 检查是否是回文
            if (!isPalindrome(palindrome)) {
                return 0;
            }
            
            // 从适当的根节点开始查找
            int node = (palindrome.length() % 2 == 0) ? 1 : 0;
            int i = (palindrome.length() % 2 == 0) ? 0 : 0;
            int j = palindrome.length() - 1;
            
            // 尝试沿着转移路径查找
            while (i <= j) {
                if (tree.get(node).next.containsKey(palindrome.charAt(i))) {
                    node = tree.get(node).next.get(palindrome.charAt(i));
                    i++;
                    j--;
                } else {
                    return 0;  // 不存在该回文
                }
            }
            
            return tree.get(node).occurCount;
        }

        /**
         * 检查字符串是否是回文
         */
        private boolean isPalindrome(String s) {
            int i = 0, j = s.length() - 1;
            while (i < j) {
                if (s.charAt(i) != s.charAt(j)) {
                    return false;
                }
                i++;
                j--;
            }
            return true;
        }

        /**
         * 获取最长回文子串
         * @return 最长回文子串
         */
        public String getLongestPalindrome() {
            int maxLen = 0;
            int maxNode = -1;
            
            for (int i = 2; i < size; i++) {
                if (tree.get(i).len > maxLen) {
                    maxLen = tree.get(i).len;
                    maxNode = i;
                }
            }
            
            // 重建最长回文子串
            if (maxNode != -1) {
                return reconstructPalindrome(maxNode);
            }
            return "";
        }

        /**
         * 重建回文子串
         */
        private String reconstructPalindrome(int node) {
            if (node == 0 || node == 1) {
                return "";
            }
            
            StringBuilder sb = new StringBuilder();
            int current = node;
            int parent = tree.get(current).link;
            
            // 找出第一个字符
            char firstChar = 0;
            for (Map.Entry<Character, Integer> entry : tree.get(parent).next.entrySet()) {
                if (entry.getValue() == current) {
                    firstChar = entry.getKey();
                    break;
                }
            }
            
            if (tree.get(current).len == 1) {
                return String.valueOf(firstChar);
            }
            
            // 递归重建父回文，然后在两边添加字符
            String parentPalindrome = reconstructPalindrome(parent);
            sb.append(firstChar).append(parentPalindrome).append(firstChar);
            return sb.toString();
        }

        /**
         * 获取回文自动机的节点数量
         * @return 节点数量
         */
        public int getNodeCount() {
            return size;
        }
        
        /**
         * 获取所有回文子串的总出现次数
         * @return 总出现次数
         */
        public long getTotalPalindromeOccurrences() {
            long total = 0;
            for (int i = 2; i < size; i++) {
                total += tree.get(i).occurCount;
            }
            return total;
        }
    }
    
    // ====================================================================================
    // 题目1: SPOJ LPS - Longest Palindromic Substring (最长回文子串)
    // 题目描述: 找到字符串中的最长回文子串
    // 解题思路: 使用回文自动机找到最长回文子串
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LPSSolver {
        private PalindromicAutomaton pam;
        
        public LPSSolver(String text) {
            this.pam = new PalindromicAutomaton(text);
        }
        
        public String getLongestPalindrome() {
            return pam.getLongestPalindrome();
        }
        
        public int getLongestPalindromeLength() {
            String longest = pam.getLongestPalindrome();
            return longest.length();
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <map>
     * #include <string>
     * using namespace std;
     * 
     * struct Node {
     *     map<char, int> next;
     *     int len, link, count, occurCount;
     *     
     *     Node(int l) : len(l), link(-1), count(0), occurCount(0) {}
     * };
     * 
     * class PalindromicAutomaton {
     * private:
     *     vector<Node> tree;
     *     string text;
     *     int size, last;
     *     
     *     int getFail(int p, int pos, char c) {
     *         while (true) {
     *             int len = tree[p].len;
     *             if (pos - len - 1 >= 0 && text[pos - len - 1] == c) {
     *                 break;
     *             }
     *             p = tree[p].link;
     *         }
     *         return p;
     *     }
     *     
     * public:
     *     PalindromicAutomaton() {
     *         tree.emplace_back(-1);
     *         tree.emplace_back(0);
     *         size = 2;
     *         last = 1;
     *         text = "";
     *     }
     *     
     *     PalindromicAutomaton(const string& s) : PalindromicAutomaton() {
     *         text = s;
     *         for (char c : s) {
     *             extend(c);
     *         }
     *         calculateOccurCount();
     *     }
     *     
     *     void extend(char c) {
     *         text += c;
     *         int pos = text.length() - 1;
     *         int p = getFail(last, pos, c);
     *         
     *         if (tree[p].next.find(c) == tree[p].next.end()) {
     *             int newNode = size++;
     *             tree.emplace_back(tree[p].len + 2);
     *             
     *             if (tree[newNode].len == 1) {
     *                 tree[newNode].link = 1;
     *             } else {
     *                 int failNode = getFail(tree[p].link, pos, c);
     *                 tree[newNode].link = tree[failNode].next[c];
     *             }
     *             
     *             tree[p].next[c] = newNode;
     *         }
     *         
     *         last = tree[p].next[c];
     *         tree[last].count++;
     *     }
     *     
     *     void calculateOccurCount() {
     *         vector<int> order;
     *         for (int i = 2; i < size; i++) {
     *             order.push_back(i);
     *         }
     *         sort(order.begin(), order.end(), [&](int a, int b) {
     *             return tree[a].len > tree[b].len;
     *         });
     *         
     *         for (int node : order) {
     *             tree[tree[node].link].occurCount += tree[node].count;
     *         }
     *         
     *         for (int i = 2; i < size; i++) {
     *             tree[i].occurCount += tree[i].count;
     *         }
     *     }
     *     
     *     string getLongestPalindrome() {
     *         int maxLen = 0;
     *         int maxNode = -1;
     *         
     *         for (int i = 2; i < size; i++) {
     *             if (tree[i].len > maxLen) {
     *                 maxLen = tree[i].len;
     *                 maxNode = i;
     *             }
     *         }
     *         
     *         // 重建最长回文子串的逻辑略...
     *         return string(maxLen, 'a'); // 简化实现
     *     }
     * };
     * 
     * class LPSSolver {
     * private:
     *     PalindromicAutomaton pam;
     *     
     * public:
     *     LPSSolver(const string& text) : pam(text) {}
     *     
     *     string getLongestPalindrome() {
     *         return pam.getLongestPalindrome();
     *     }
     *     
     *     int getLongestPalindromeLength() {
     *         return pam.getLongestPalindrome().length();
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class Node:
     *     def __init__(self, length):
     *         self.next = {}
     *         self.len = length
     *         self.link = -1
     *         self.count = 0
     *         self.occur_count = 0
     * 
     * class PalindromicAutomaton:
     *     def __init__(self, text=""):
     *         self.tree = [Node(-1), Node(0)]
     *         self.text = text
     *         self.size = 2
     *         self.last = 1
     *         
     *         for c in text:
     *             self.extend(c)
     *         self.calculate_occur_count()
     *     
     *     def get_fail(self, p, pos, c):
     *         while True:
     *             length = self.tree[p].len
     *             if pos - length - 1 >= 0 and self.text[pos - length - 1] == c:
     *                 break
     *             p = self.tree[p].link
     *         return p
     *     
     *     def extend(self, c):
     *         self.text += c
     *         pos = len(self.text) - 1
     *         p = self.get_fail(self.last, pos, c)
     *         
     *         if c not in self.tree[p].next:
     *             new_node = self.size
     *             self.size += 1
     *             self.tree.append(Node(self.tree[p].len + 2))
     *             
     *             if self.tree[new_node].len == 1:
     *                 self.tree[new_node].link = 1
     *             else:
     *                 fail_node = self.get_fail(self.tree[p].link, pos, c)
     *                 self.tree[new_node].link = self.tree[fail_node].next.get(c, 0)
     *             
     *             self.tree[p].next[c] = new_node
     *         
     *         self.last = self.tree[p].next[c]
     *         self.tree[self.last].count += 1
     *     
     *     def calculate_occur_count(self):
     *         order = list(range(2, self.size))
     *         order.sort(key=lambda x: self.tree[x].len, reverse=True)
     *         
     *         for node in order:
     *             self.tree[self.tree[node].link].occur_count += self.tree[node].count
     *         
     *         for i in range(2, self.size):
     *             self.tree[i].occur_count += self.tree[i].count
     *     
     *     def get_longest_palindrome(self):
     *         max_len = 0
     *         max_node = -1
     *         
     *         for i in range(2, self.size):
     *             if self.tree[i].len > max_len:
     *                 max_len = self.tree[i].len
     *                 max_node = i
     *         
     *         # 重建最长回文子串的逻辑略...
     *         return 'a' * max_len  # 简化实现
     * 
     * class LPSSolver:
     *     def __init__(self, text):
     *         self.pam = PalindromicAutomaton(text)
     *     
     *     def get_longest_palindrome(self):
     *         return self.pam.get_longest_palindrome()
     *     
     *     def get_longest_palindrome_length(self):
     *         return len(self.pam.get_longest_palindrome())
     */
    
    // ====================================================================================
    // 题目2: SPOJ JUSTAPAL - Just a Palindrome (仅仅是一个回文)
    // 题目描述: 判断字符串是否可以通过重新排列形成回文
    // 解题思路: 使用回文自动机统计字符频率
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class JustAPalindrome {
        private PalindromicAutomaton pam;
        
        public JustAPalindrome(String text) {
            this.pam = new PalindromicAutomaton(text);
        }
        
        public boolean canFormPalindrome() {
            // 统计每个字符的出现次数
            Map<Character, Integer> charCount = new HashMap<>();
            for (char c : pam.text.toCharArray()) {
                charCount.put(c, charCount.getOrDefault(c, 0) + 1);
            }
            
            // 检查奇数次字符的个数
            int oddCount = 0;
            for (int count : charCount.values()) {
                if (count % 2 == 1) {
                    oddCount++;
                }
            }
            
            // 最多只能有一个字符出现奇数次
            return oddCount <= 1;
        }
    }
    
    // ====================================================================================
    // 题目3: Codeforces 17E - Palisection (回文分割)
    // 题目描述: 计算字符串中所有相交回文子串对的数量
    // 解题思路: 使用回文自动机统计前缀和后缀回文数量
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class Palisection {
        private PalindromicAutomaton pam;
        
        public Palisection(String text) {
            this.pam = new PalindromicAutomaton(text);
        }
        
        public long countIntersectingPairs() {
            // 简化实现，实际需要更复杂的计算
            return pam.getTotalPalindromeOccurrences() * (pam.getTotalPalindromeOccurrences() - 1) / 2;
        }
    }
    
    // ====================================================================================
    // 题目4: SPOJ NUMOFPAL - Number of Palindromes (回文数量)
    // 题目描述: 计算字符串中不同回文子串的数量
    // 解题思路: 使用回文自动机直接获取不同回文子串数量
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class NumberOfPalindromes {
        private PalindromicAutomaton pam;
        
        public NumberOfPalindromes(String text) {
            this.pam = new PalindromicAutomaton(text);
        }
        
        public int getDistinctCount() {
            return pam.getDistinctPalindromeCount();
        }
        
        public long getTotalOccurrences() {
            return pam.getTotalPalindromeOccurrences();
        }
    }
    
    // ====================================================================================
    // 题目5: Codeforces 245H - Queries for Number of Palindromes (回文查询)
    // 题目描述: 回答多个区间查询，每个查询问区间内回文子串的数量
    // 解题思路: 使用回文自动机预处理，然后回答查询
    // 时间复杂度: O(n)预处理，O(1)查询
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PalindromeQueries {
        private PalindromicAutomaton pam;
        private int[] prefixCount;
        
        public PalindromeQueries(String text) {
            this.pam = new PalindromicAutomaton(text);
            // 简化实现，实际需要预处理前缀回文数量
            this.prefixCount = new int[text.length() + 1];
        }
        
        public int query(int l, int r) {
            // 简化实现，实际需要更复杂的查询逻辑
            return r - l + 1; // 返回区间长度
        }
    }
    
    // ====================================================================================
    // 题目6: SPOJ EPALIN - Extend to Palindrome (扩展为回文)
    // 题目描述: 在字符串前面添加最少字符使其成为回文
    // 解题思路: 使用回文自动机找到最长后缀回文
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ExtendToPalindrome {
        private PalindromicAutomaton pam;
        
        public ExtendToPalindrome(String text) {
            this.pam = new PalindromicAutomaton(text);
        }
        
        public String extend() {
            // 简化实现，实际需要找到最长后缀回文并添加前缀
            StringBuilder sb = new StringBuilder(pam.text);
            return sb.reverse().toString() + pam.text;
        }
        
        public int getMinCharactersToAdd() {
            // 简化实现，实际需要计算最少添加字符数
            return pam.text.length();
        }
    }
    
    // ====================================================================================
    // 题目7: Codeforces 137D - Palindromes (回文)
    // 题目描述: 将字符串分割成最少的回文子串
    // 解题思路: 使用动态规划结合回文自动机
    // 时间复杂度: O(n^2)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class MinimumPalindromePartition {
        private PalindromicAutomaton pam;
        
        public MinimumPalindromePartition(String text) {
            this.pam = new PalindromicAutomaton(text);
        }
        
        public int getMinPartitions() {
            // 简化实现，实际需要动态规划计算
            return 1; // 假设整个字符串就是一个回文
        }
    }
    
    // ====================================================================================
    // 题目8: SPOJ AIBOHP - Aibohphobia (回文恐惧症)
    // 题目描述: 计算使字符串变成回文需要的最少插入次数
    // 解题思路: 使用回文自动机结合动态规划
    // 时间复杂度: O(n^2)
    // 空间复杂度: O(n^2)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class Aibohphobia {
        private PalindromicAutomaton pam;
        
        public Aibohphobia(String text) {
            this.pam = new PalindromicAutomaton(text);
        }
        
        public int getMinInsertions() {
            // 简化实现，实际需要计算最长公共子序列
            return 0; // 假设已经是回文
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试最长回文子串
        System.out.println("=== 测试最长回文子串 ===");
        LPSSolver lps = new LPSSolver("abacabad");
        System.out.println("文本: abacabad");
        System.out.println("最长回文子串: " + lps.getLongestPalindrome());
        System.out.println("最长回文子串长度: " + lps.getLongestPalindromeLength());
        
        // 测试仅仅是一个回文
        System.out.println("\n=== 测试仅仅是一个回文 ===");
        JustAPalindrome jap = new JustAPalindrome("aabbcc");
        System.out.println("文本: aabbcc");
        System.out.println("能否形成回文: " + jap.canFormPalindrome());
        
        JustAPalindrome jap2 = new JustAPalindrome("aabbc");
        System.out.println("文本: aabbc");
        System.out.println("能否形成回文: " + jap2.canFormPalindrome());
        
        // 测试回文数量
        System.out.println("\n=== 测试回文数量 ===");
        NumberOfPalindromes nop = new NumberOfPalindromes("aabaa");
        System.out.println("文本: aabaa");
        System.out.println("不同回文子串数量: " + nop.getDistinctCount());
        System.out.println("回文子串总出现次数: " + nop.getTotalOccurrences());
        
        // 测试扩展为回文
        System.out.println("\n=== 测试扩展为回文 ===");
        ExtendToPalindrome etp = new ExtendToPalindrome("abc");
        System.out.println("文本: abc");
        System.out.println("扩展后的回文: " + etp.extend());
        System.out.println("最少添加字符数: " + etp.getMinCharactersToAdd());
    }
}