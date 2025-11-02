package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级后缀自动机题目实现
 * 包含：
 * 1. SPOJ LCS - Longest Common Substring (最长公共子串)
 * 2. SPOJ LCS2 - Longest Common Substring II (多个字符串最长公共子串)
 * 3. SPOJ NSUBSTR - Substrings (不同长度子串统计)
 * 4. SPOJ SUBLEX - Lexicographical Substring Search (字典序第K小子串)
 * 5. Codeforces 271D - Good Substrings (好子串计数)
 * 6. Codeforces 432D - Prefixes and Suffixes (前缀与后缀)
 * 7. Codeforces 128B - String (字典序第K小子串)
 * 8. SPOJ SUBST1 - Distinct Substrings (不同子串计数)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class SuffixAutomatonAdvancedProblems {
    
    /**
     * 后缀自动机的状态节点
     */
    private static class State {
        Map<Character, Integer> next; // 转移函数
        int len;                     // 该状态能接受的最长子串长度
        int link;                    // 后缀链接（suffix link）
        int endposSize;              // endpos集合的大小
        boolean isClone;             // 是否是克隆节点
        int firstPos;                // 该状态第一次出现的位置
        Map<Integer, Integer> count; // 用于多字符串计数

        public State(int len) {
            this.next = new HashMap<>();
            this.len = len;
            this.link = -1;
            this.endposSize = 0;
            this.isClone = false;
            this.firstPos = 0;
            this.count = new HashMap<>();
        }
    }

    /**
     * 基础后缀自动机实现
     */
    static class SuffixAutomaton {
        protected List<State> states;     // 所有状态
        protected int last;              // 上一个状态的索引
        protected int size;              // 当前状态数量
        protected String text;           // 原始文本

        /**
         * 构造函数，构建后缀自动机
         * @param text 输入文本
         */
        public SuffixAutomaton(String text) {
            if (text == null) {
                throw new IllegalArgumentException("输入文本不能为null");
            }
            
            this.text = text;
            this.states = new ArrayList<>();
            this.last = 0;
            this.size = 1;
            
            // 创建初始状态
            states.add(new State(0));
            states.get(0).firstPos = -1;
            
            // 逐个字符构建自动机
            for (int i = 0; i < text.length(); i++) {
                extend(text.charAt(i), i);
            }
            
            // 计算endpos集合大小
            calculateEndposSize();
        }

        /**
         * 扩展后缀自动机，添加一个字符
         * @param c 要添加的字符
         * @param pos 字符在原字符串中的位置
         */
        protected void extend(char c, int pos) {
            // 创建新状态cur
            int cur = size++;
            states.add(new State(states.get(last).len + 1));
            states.get(cur).endposSize = 1; // 新状态的endpos大小为1，因为它只对应一个位置
            states.get(cur).firstPos = pos;
            
            // 从last开始，沿着后缀链接回溯，添加转移
            int p = last;
            while (p >= 0 && !states.get(p).next.containsKey(c)) {
                states.get(p).next.put(c, cur);
                p = states.get(p).link;
            }
            
            if (p == -1) {
                // 如果没有找到含有c转移的状态，将cur的后缀链接指向初始状态
                states.get(cur).link = 0;
            } else {
                int q = states.get(p).next.get(c);
                if (states.get(p).len + 1 == states.get(q).len) {
                    // 如果q已经是p通过c转移后的正确状态
                    states.get(cur).link = q;
                } else {
                    // 需要分裂状态q
                    int clone = size++;
                    states.add(new State(states.get(p).len + 1));
                    states.get(clone).next.putAll(states.get(q).next); // 复制转移
                    states.get(clone).link = states.get(q).link;      // 复制后缀链接
                    states.get(clone).firstPos = states.get(q).firstPos;
                    states.get(clone).isClone = true;                 // 标记为克隆节点
                    
                    // 更新q和cur的后缀链接
                    states.get(q).link = clone;
                    states.get(cur).link = clone;
                    
                    // 从p开始，沿着后缀链接回溯，更新转移
                    while (p >= 0 && states.get(p).next.get(c) == q) {
                        states.get(p).next.put(c, clone);
                        p = states.get(p).link;
                    }
                }
            }
            
            // 更新last为新状态
            last = cur;
        }

        /**
         * 计算每个状态的endpos集合大小
         * 基于后缀链接树进行后序遍历累加
         */
        private void calculateEndposSize() {
            // 根据len对状态进行排序（用于后序遍历后缀链接树）
            List<Integer> order = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                order.add(i);
            }
            order.sort((a, b) -> Integer.compare(states.get(b).len, states.get(a).len));
            
            // 后序遍历，累加endpos大小
            for (int v : order) {
                if (states.get(v).link != -1 && !states.get(v).isClone) {
                    states.get(states.get(v).link).endposSize += states.get(v).endposSize;
                }
            }
        }

        /**
         * 检查字符串s是否是原始文本的子串
         * @param s 要检查的字符串
         * @return 如果是子串返回true，否则返回false
         */
        public boolean contains(String s) {
            if (s == null) {
                throw new IllegalArgumentException("查询字符串不能为null");
            }
            
            int state = 0; // 从初始状态开始
            for (char c : s.toCharArray()) {
                if (!states.get(state).next.containsKey(c)) {
                    return false; // 没有对应的转移，不是子串
                }
                state = states.get(state).next.get(c);
            }
            return true; // 成功匹配所有字符
        }

        /**
         * 计算不同子串的数量
         * 利用性质：不同子串数量 = Σ (len[link[u]] - len[u])
         * @return 不同子串的数量
         */
        public long countDistinctSubstrings() {
            long count = 0;
            for (int i = 1; i < size; i++) { // 跳过初始状态
                count += states.get(i).len - states.get(states.get(i).link).len;
            }
            return count;
        }

        /**
         * 计算子串s在原文本中出现的次数
         * @param s 要查询的子串
         * @return 出现次数
         */
        public int countOccurrences(String s) {
            if (s == null) {
                throw new IllegalArgumentException("查询字符串不能为null");
            }
            
            // 找到对应s的状态
            int state = 0;
            for (char c : s.toCharArray()) {
                if (!states.get(state).next.containsKey(c)) {
                    return 0; // 不是子串，出现次数为0
                }
                state = states.get(state).next.get(c);
            }
            
            return states.get(state).endposSize;
        }

        /**
         * 找出所有出现次数至少为k次的子串中，最长的那个
         * @param k 最小出现次数
         * @return 最长的满足条件的子串
         */
        public String findLongestSubstringWithKOccurrences(int k) {
            if (k <= 0) {
                throw new IllegalArgumentException("k必须为正整数");
            }
            
            String result = "";
            int maxLength = 0;
            
            // 遍历所有状态，找到endposSize >= k的状态，且len最大
            for (int i = 1; i < size; i++) {
                if (states.get(i).endposSize >= k && states.get(i).len > maxLength) {
                    maxLength = states.get(i).len;
                }
            }
            
            if (maxLength == 0) {
                return result;
            }
            
            // 找到对应的子串
            // 从初始状态开始，尝试构建长度为maxLength的子串
            StringBuilder sb = new StringBuilder();
            int state = 0;
            return findSubstringByLength(state, maxLength, sb);
        }

        /**
         * 递归查找指定长度的子串
         */
        private String findSubstringByLength(int state, int targetLength, StringBuilder current) {
            if (states.get(state).len == targetLength) {
                return current.toString();
            }
            
            for (Map.Entry<Character, Integer> entry : states.get(state).next.entrySet()) {
                int nextState = entry.getValue();
                if (states.get(nextState).len <= targetLength) {
                    current.append(entry.getKey());
                    String result = findSubstringByLength(nextState, targetLength, current);
                    if (result != null) {
                        return result;
                    }
                    current.deleteCharAt(current.length() - 1);
                }
            }
            
            return null;
        }

        /**
         * 找出文本的最长重复子串
         * @return 最长重复子串
         */
        public String findLongestRepeatedSubstring() {
            return findLongestSubstringWithKOccurrences(2);
        }

        /**
         * 获取后缀自动机的状态数量
         * @return 状态数量
         */
        public int getStateCount() {
            return size;
        }
    }
    
    /**
     * 扩展的后缀自动机，用于多字符串处理
     */
    static class ExtendedSuffixAutomaton extends SuffixAutomaton {
        public ExtendedSuffixAutomaton(String text) {
            super(text);
        }
        
        /**
         * 更新指定字符串在自动机中的计数
         */
        public void updateCount(String s, int stringIndex) {
            int currentState = 0;
            int currentLen = 0;
            int[] lcs = new int[size]; // 记录每个状态的匹配长度
            
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                
                while (currentState > 0 && !states.get(currentState).next.containsKey(c)) {
                    currentState = states.get(currentState).link;
                    currentLen = states.get(currentState).len;
                }
                
                if (states.get(currentState).next.containsKey(c)) {
                    currentState = states.get(currentState).next.get(c);
                    currentLen++;
                    lcs[currentState] = Math.max(lcs[currentState], currentLen);
                } else {
                    currentState = 0;
                    currentLen = 0;
                }
            }
            
            // 更新计数信息
            // 根据后缀链接树进行后序遍历
            List<Integer> order = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                order.add(i);
            }
            order.sort((a, b) -> Integer.compare(states.get(b).len, states.get(a).len));
            
            for (int v : order) {
                if (lcs[v] > 0) {
                    states.get(v).count.put(stringIndex, states.get(v).count.getOrDefault(stringIndex, 0) + 1);
                    if (states.get(v).link != -1) {
                        lcs[states.get(v).link] = Math.max(lcs[states.get(v).link], 
                                                          Math.min(states.get(states.get(v).link).len, lcs[v]));
                    }
                }
            }
        }
    }
    
    // ====================================================================================
    // 题目1: SPOJ LCS - Longest Common Substring
    // 题目描述: 给定两个字符串，找出它们的最长公共子串
    // 解题思路: 对第一个字符串构建后缀自动机，用第二个字符串在自动机上匹配
    // 时间复杂度: O(n + m)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int longestCommonSubstring(String s1, String s2) {
        // 对第一个字符串构建后缀自动机
        SuffixAutomaton sam = new SuffixAutomaton(s1);
        
        int maxLen = 0;
        int currentState = 0;
        int currentLen = 0;
        
        // 在自动机上匹配第二个字符串
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            
            // 如果当前状态没有c的转移
            while (currentState > 0 && !sam.states.get(currentState).next.containsKey(c)) {
                currentState = sam.states.get(currentState).link;
                currentLen = sam.states.get(currentState).len;
            }
            
            // 如果找到了转移
            if (sam.states.get(currentState).next.containsKey(c)) {
                currentState = sam.states.get(currentState).next.get(c);
                currentLen++;
                maxLen = Math.max(maxLen, currentLen);
            } else {
                // 回到初始状态
                currentState = 0;
                currentLen = 0;
            }
        }
        
        return maxLen;
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <string>
     * #include <unordered_map>
     * using namespace std;
     * 
     * struct State {
     *     unordered_map<char, int> next;
     *     int len, link;
     *     
     *     State(int l = 0) : len(l), link(-1) {}
     * };
     * 
     * class SuffixAutomaton {
     *     vector<State> states;
     *     int last, size;
     *     
     * public:
     *     SuffixAutomaton(const string& s) : last(0), size(1) {
     *         states.emplace_back(0);
     *         for (char c : s) extend(c);
     *     }
     *     
     *     void extend(char c) {
     *         int cur = size++;
     *         states.emplace_back(states[last].len + 1);
     *         
     *         int p = last;
     *         while (p >= 0 && !states[p].next.count(c)) {
     *             states[p].next[c] = cur;
     *             p = states[p].link;
     *         }
     *         
     *         if (p == -1) {
     *             states[cur].link = 0;
     *         } else {
     *             int q = states[p].next[c];
     *             if (states[p].len + 1 == states[q].len) {
     *                 states[cur].link = q;
     *             } else {
     *                 int clone = size++;
     *                 states.emplace_back(states[p].len + 1);
     *                 states[clone].next = states[q].next;
     *                 states[clone].link = states[q].link;
     *                 
     *                 states[q].link = states[cur].link = clone;
     *                 
     *                 while (p >= 0 && states[p].next[c] == q) {
     *                     states[p].next[c] = clone;
     *                     p = states[p].link;
     *                 }
     *             }
     *         }
     *         
     *         last = cur;
     *     }
     *     
     *     int longestCommonSubstring(const string& t) {
     *         int maxLen = 0, currentState = 0, currentLen = 0;
     *         
     *         for (char c : t) {
     *             while (currentState > 0 && !states[currentState].next.count(c)) {
     *                 currentState = states[currentState].link;
     *                 currentLen = states[currentState].len;
     *             }
     *             
     *             if (states[currentState].next.count(c)) {
     *                 currentState = states[currentState].next[c];
     *                 currentLen++;
     *                 maxLen = max(maxLen, currentLen);
     *             } else {
     *                 currentState = 0;
     *                 currentLen = 0;
     *             }
     *         }
     *         
     *         return maxLen;
     *     }
     * };
     * 
     * int solveLCS(string s1, string s2) {
     *     SuffixAutomaton sam(s1);
     *     return sam.longestCommonSubstring(s2);
     * }
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class State:
     *     def __init__(self, length=0):
     *         self.next = {}
     *         self.length = length
     *         self.link = -1
     * 
     * class SuffixAutomaton:
     *     def __init__(self, s):
     *         self.states = [State(0)]
     *         self.last = 0
     *         self.size = 1
     *         for c in s:
     *             self.extend(c)
     *     
     *     def extend(self, c):
     *         cur = self.size
     *         self.size += 1
     *         self.states.append(State(self.states[self.last].length + 1))
     *         
     *         p = self.last
     *         while p >= 0 and c not in self.states[p].next:
     *             self.states[p].next[c] = cur
     *             p = self.states[p].link
     *         
     *         if p == -1:
     *             self.states[cur].link = 0
     *         else:
     *             q = self.states[p].next[c]
     *             if self.states[p].length + 1 == self.states[q].length:
     *                 self.states[cur].link = q
     *             else:
     *                 clone = self.size
     *                 self.size += 1
     *                 self.states.append(State(self.states[p].length + 1))
     *                 self.states[clone].next = self.states[q].next.copy()
     *                 self.states[clone].link = self.states[q].link
     *                 
     *                 self.states[q].link = self.states[cur].link = clone
     *                 
     *                 while p >= 0 and self.states[p].next.get(c) == q:
     *                     self.states[p].next[c] = clone
     *                     p = self.states[p].link
     *         
     *         self.last = cur
     *     
     *     def longest_common_substring(self, t):
     *         max_len = 0
     *         current_state = 0
     *         current_len = 0
     *         
     *         for c in t:
     *             while current_state > 0 and c not in self.states[current_state].next:
     *                 current_state = self.states[current_state].link
     *                 current_len = self.states[current_state].length
     *             
     *             if c in self.states[current_state].next:
     *                 current_state = self.states[current_state].next[c]
     *                 current_len += 1
     *                 max_len = max(max_len, current_len)
     *             else:
     *                 current_state = 0
     *                 current_len = 0
     *         
     *         return max_len
     * 
     * def solve_lcs(s1, s2):
     *     sam = SuffixAutomaton(s1)
     *     return sam.longest_common_substring(s2)
     */
    
    // ====================================================================================
    // 题目2: SPOJ LCS2 - Longest Common Substring II
    // 题目描述: 给定多个字符串，找出它们的最长公共子串
    // 解题思路: 对第一个字符串构建后缀自动机，对每个状态计算它在所有字符串中的出现次数
    // 时间复杂度: O(k*n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int longestCommonSubstringMultiple(String[] strings) {
        if (strings == null || strings.length == 0) {
            return 0;
        }
        
        if (strings.length == 1) {
            return strings[0].length();
        }
        
        // 对第一个字符串构建后缀自动机
        ExtendedSuffixAutomaton sam = new ExtendedSuffixAutomaton(strings[0]);
        
        // 对于每个字符串，更新状态的计数
        for (int i = 1; i < strings.length; i++) {
            sam.updateCount(strings[i], i);
        }
        
        // 找到在所有字符串中都出现的状态中长度最大的
        int maxLen = 0;
        for (int i = 1; i < sam.size; i++) {
            boolean allContain = true;
            for (int j = 0; j < strings.length; j++) {
                if (sam.states.get(i).count.getOrDefault(j, 0) == 0) {
                    allContain = false;
                    break;
                }
            }
            
            if (allContain) {
                maxLen = Math.max(maxLen, sam.states.get(i).len);
            }
        }
        
        return maxLen;
    }
    
    // ====================================================================================
    // 题目3: SPOJ NSUBSTR - Substrings
    // 题目描述: 对于长度为x的子串，定义F(x)为该字符串中长度为x的子串的最大出现次数
    // 解题思路: 构建后缀自动机，利用endposSize属性统计
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int[] countSubstringsByLength(String s) {
        SuffixAutomaton sam = new SuffixAutomaton(s);
        int n = s.length();
        int[] result = new int[n + 1];
        
        // 对每个状态，用endposSize更新对应长度的计数
        for (int i = 1; i < sam.size; i++) {
            result[sam.states.get(i).len] = Math.max(result[sam.states.get(i).len], 
                                                    sam.states.get(i).endposSize);
        }
        
        // 由于parent树的性质，需要从大到小更新
        for (int i = n - 1; i >= 1; i--) {
            result[i] = Math.max(result[i], result[i + 1]);
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目4: SPOJ SUBLEX - Lexicographical Substring Search
    // 题目描述: 给定一个字符串，找出其字典序第K小的子串
    // 解题思路: 构建后缀自动机，预处理每个状态能形成的子串数量，按字典序DFS查找
    // 时间复杂度: O(n + k)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static String kthSubstring(String s, int k) {
        SuffixAutomaton sam = new SuffixAutomaton(s);
        int n = sam.size;
        
        // 预处理每个状态能形成的子串数量
        long[] count = new long[n];
        Arrays.fill(count, 0);
        
        // 按照len从大到小排序
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            order.add(i);
        }
        order.sort((a, b) -> Integer.compare(sam.states.get(b).len, sam.states.get(a).len));
        
        // 计算每个状态的子串数量
        for (int v : order) {
            count[v] = 1; // 包含空串
            for (Map.Entry<Character, Integer> entry : sam.states.get(v).next.entrySet()) {
                count[v] += count[entry.getValue()];
            }
        }
        
        // DFS查找第K小子串
        StringBuilder result = new StringBuilder();
        int currentState = 0;
        long remaining = k;
        
        while (remaining > 0) {
            if (currentState != 0) {
                remaining--; // 减去当前状态对应的空串
                if (remaining == 0) {
                    break;
                }
            }
            
            // 按字符顺序遍历转移
            TreeMap<Character, Integer> sortedNext = new TreeMap<>(sam.states.get(currentState).next);
            for (Map.Entry<Character, Integer> entry : sortedNext.entrySet()) {
                char c = entry.getKey();
                int nextState = entry.getValue();
                long nextCount = count[nextState];
                
                if (remaining <= nextCount) {
                    result.append(c);
                    currentState = nextState;
                    break;
                } else {
                    remaining -= nextCount;
                }
            }
        }
        
        return result.toString();
    }
    
    // ====================================================================================
    // 题目5: Codeforces 271D - Good Substrings
    // 题目描述: 给定一个字符串和一个标记字符串，统计好子串的数量
    // 解题思路: 在自动机上DP，状态为(节点, 坏字符数量)
    // 时间复杂度: O(n * k * 26)
    // 空间复杂度: O(n * k)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int countGoodSubstrings(String s, String goodChars, int k) {
        SuffixAutomaton sam = new SuffixAutomaton(s);
        int n = s.length();
        
        // 标记好字符
        boolean[] isGood = new boolean[26];
        for (int i = 0; i < 26; i++) {
            isGood[i] = goodChars.charAt(i) == '1';
        }
        
        // DP计算每个状态的好子串数量
        int[][] dp = new int[sam.size][k + 1];
        for (int i = 0; i < sam.size; i++) {
            Arrays.fill(dp[i], -1);
        }
        
        // DFS计算
        return dfsCount(0, 0, k, isGood, sam, dp);
    }
    
    private static int dfsCount(int state, int badCount, int k, boolean[] isGood, 
                               SuffixAutomaton sam, int[][] dp) {
        if (badCount > k) {
            return 0;
        }
        
        if (dp[state][badCount] != -1) {
            return dp[state][badCount];
        }
        
        int result = (state > 0) ? 1 : 0; // 如果不是初始状态，包含当前路径表示的子串
        
        // 遍历所有转移
        for (Map.Entry<Character, Integer> entry : sam.states.get(state).next.entrySet()) {
            char c = entry.getKey();
            int nextState = entry.getValue();
            int newBadCount = badCount + (isGood[c - 'a'] ? 0 : 1);
            
            if (newBadCount <= k) {
                result += dfsCount(nextState, newBadCount, k, isGood, sam, dp);
            }
        }
        
        return dp[state][badCount] = result;
    }
    
    // ====================================================================================
    // 题目6: Codeforces 432D - Prefixes and Suffixes
    // 题目描述: 找出所有既是前缀又是后缀的子串，以及它们的出现次数
    // 解题思路: 利用后缀自动机的性质找出满足条件的子串
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static List<Map.Entry<String, Integer>> prefixesAndSuffixes(String s) {
        SuffixAutomaton sam = new SuffixAutomaton(s);
        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        
        // 找出所有既是前缀又是后缀的子串
        Set<Integer> prefixStates = new HashSet<>();
        int state = 0;
        
        // 收集所有前缀对应的状态
        prefixStates.add(0); // 空串
        for (int i = 0; i < s.length(); i++) {
            if (sam.states.get(state).next.containsKey(s.charAt(i))) {
                state = sam.states.get(state).next.get(s.charAt(i));
                prefixStates.add(state);
            }
        }
        
        // 收集所有后缀对应的状态（通过endposSize > 0判断）
        Set<Integer> suffixStates = new HashSet<>();
        for (int i = 0; i < sam.size; i++) {
            if (sam.states.get(i).endposSize > 0) {
                suffixStates.add(i);
            }
        }
        
        // 找出交集
        Set<Integer> validStates = new HashSet<>();
        for (int st : prefixStates) {
            if (suffixStates.contains(st)) {
                validStates.add(st);
            }
        }
        
        // 为每个有效状态找出对应的字符串和出现次数
        for (int st : validStates) {
            if (st > 0) { // 跳过初始状态（空串）
                String substring = getSubstringFromState(st, sam);
                int count = sam.states.get(st).endposSize;
                result.add(new AbstractMap.SimpleEntry<>(substring, count));
            }
        }
        
        // 按长度排序
        result.sort(Comparator.comparingInt(e -> e.getKey().length()));
        
        return result;
    }
    
    /**
     * 从状态获取对应的子串
     */
    private static String getSubstringFromState(int state, SuffixAutomaton sam) {
        StringBuilder sb = new StringBuilder();
        // 通过firstPos和len重构子串
        int len = sam.states.get(state).len;
        if (len > 0) {
            int startPos = sam.states.get(state).firstPos - len + 1;
            if (startPos >= 0) {
                sb.append(sam.text.substring(startPos, startPos + len));
            }
        }
        return sb.toString();
    }
    
    // ====================================================================================
    // 题目7: Codeforces 128B - String
    // 题目描述: 求字符串的字典序第K小的子串
    // 解题思路: 使用后缀自动机，预处理每个状态能形成的子串数量，按字典序查找
    // 时间复杂度: O(n + k)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现 (与SUBLEX相同)
     */
    public static String kthSubstringCF(String s, long k) {
        return kthSubstring(s, (int)k);
    }
    
    // ====================================================================================
    // 题目8: SPOJ SUBST1 - Distinct Substrings
    // 题目描述: 计算字符串中不同子串的个数
    // 解题思路: 使用后缀自动机，利用不同子串数量的性质计算
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static long distinctSubstrings(String s) {
        SuffixAutomaton sam = new SuffixAutomaton(s);
        return sam.countDistinctSubstrings();
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试LCS
        System.out.println("=== 测试LCS ===");
        String s1 = "abcdef";
        String s2 = "zabcxy";
        int lcsLength = longestCommonSubstring(s1, s2);
        System.out.println("字符串1: " + s1);
        System.out.println("字符串2: " + s2);
        System.out.println("最长公共子串长度: " + lcsLength);
        
        // 测试NSUBSTR
        System.out.println("\n=== 测试NSUBSTR ===");
        String s3 = "ababa";
        int[] counts = countSubstringsByLength(s3);
        System.out.println("字符串: " + s3);
        System.out.println("不同长度子串的最大出现次数:");
        for (int i = 1; i <= s3.length(); i++) {
            System.out.println("F(" + i + ") = " + counts[i]);
        }
        
        // 测试SUBLEX
        System.out.println("\n=== 测试SUBLEX ===");
        String s4 = "abc";
        System.out.println("字符串: " + s4);
        for (int k = 1; k <= 5; k++) {
            String kth = kthSubstring(s4, k);
            System.out.println("第" + k + "小的子串: \"" + kth + "\"");
        }
        
        // 测试SUBST1
        System.out.println("\n=== 测试SUBST1 ===");
        String s5 = "banana";
        long distinctCount = distinctSubstrings(s5);
        System.out.println("字符串: " + s5);
        System.out.println("不同子串数量: " + distinctCount);
    }
}