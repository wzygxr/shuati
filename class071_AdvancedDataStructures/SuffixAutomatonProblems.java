package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 后缀自动机相关题目实现
 * 
 * 本文件包含了多个使用后缀自动机解决的经典算法题目：
 * 1. SPOJ LCS - Longest Common Substring（最长公共子串）
 * 2. SPOJ LCS2 - Longest Common Substring II（多个字符串的最长公共子串）
 * 3. SPOJ NSUBSTR - Substrings（统计不同长度子串的最大出现次数）
 * 4. SPOJ SUBLEX - Lexicographical Substring Search（字典序第K小子串）
 * 5. Codeforces 271D - Good Substrings（好子串计数）
 * 6. Codeforces 432D - Prefixes and Suffixes（前缀与后缀）
 * 
 * 时间复杂度：构建O(n)，查询O(m)
 * 空间复杂度：O(n)
 */
public class SuffixAutomatonProblems {
    
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
        int maxLen;                  // 该状态能接受的最长子串长度（用于LCS）
        Map<Integer, Integer> count; // 用于LCS2，记录每个字符串的出现次数

        public State(int len) {
            this.next = new HashMap<>();
            this.len = len;
            this.link = -1;
            this.endposSize = 0;
            this.isClone = false;
            this.firstPos = 0;
            this.maxLen = 0;
            this.count = new HashMap<>();
        }
    }

    /**
     * 基础后缀自动机实现
     */
    static class SuffixAutomaton {
        private List<State> states;     // 所有状态
        private int last;              // 上一个状态的索引
        private int size;              // 当前状态数量
        private String text;           // 原始文本

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
        private void extend(char c, int pos) {
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
     * 1. SPOJ LCS - Longest Common Substring（最长公共子串）
     * 题目描述：给定两个字符串，找出它们的最长公共子串
     * 来源：https://www.spoj.com/problems/LCS/
     * 
     * 解题思路：
     * 1. 对第一个字符串构建后缀自动机
     * 2. 用第二个字符串在自动机上匹配，记录匹配的最大长度
     * 
     * 时间复杂度：O(n + m)，其中n是第一个字符串长度，m是第二个字符串长度
     * 空间复杂度：O(n)
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
     * 2. SPOJ LCS2 - Longest Common Substring II（多个字符串的最长公共子串）
     * 题目描述：给定多个字符串，找出它们的最长公共子串
     * 来源：https://www.spoj.com/problems/LCS2/
     * 
     * 解题思路：
     * 1. 对第一个字符串构建后缀自动机
     * 2. 对于每个状态，计算它在所有字符串中的出现次数
     * 3. 找到在所有字符串中都出现的状态中长度最大的
     * 
     * 时间复杂度：O(k*n)，其中k是字符串数量，n是字符串总长度
     * 空间复杂度：O(n)
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
    
    /**
     * 扩展的后缀自动机，用于LCS2问题
     */
    static class ExtendedSuffixAutomaton {
        private List<State> states;
        private int last;
        private int size;
        private String text;

        public ExtendedSuffixAutomaton(String text) {
            this.text = text;
            this.states = new ArrayList<>();
            this.last = 0;
            this.size = 1;
            
            states.add(new State(0));
            states.get(0).firstPos = -1;
            states.get(0).count.put(0, 1); // 第一个字符串包含初始状态
            
            for (int i = 0; i < text.length(); i++) {
                extend(text.charAt(i), i);
            }
        }

        private void extend(char c, int pos) {
            int cur = size++;
            states.add(new State(states.get(last).len + 1));
            states.get(cur).endposSize = 1;
            states.get(cur).firstPos = pos;
            states.get(cur).count.put(0, 1); // 第一个字符串包含这个状态
            
            int p = last;
            while (p >= 0 && !states.get(p).next.containsKey(c)) {
                states.get(p).next.put(c, cur);
                p = states.get(p).link;
            }
            
            if (p == -1) {
                states.get(cur).link = 0;
            } else {
                int q = states.get(p).next.get(c);
                if (states.get(p).len + 1 == states.get(q).len) {
                    states.get(cur).link = q;
                } else {
                    int clone = size++;
                    states.add(new State(states.get(p).len + 1));
                    states.get(clone).next.putAll(states.get(q).next);
                    states.get(clone).link = states.get(q).link;
                    states.get(clone).firstPos = states.get(q).firstPos;
                    states.get(clone).isClone = true;
                    // 复制计数信息
                    for (Map.Entry<Integer, Integer> entry : states.get(q).count.entrySet()) {
                        states.get(clone).count.put(entry.getKey(), entry.getValue());
                    }
                    
                    states.get(q).link = clone;
                    states.get(cur).link = clone;
                    
                    while (p >= 0 && states.get(p).next.get(c) == q) {
                        states.get(p).next.put(c, clone);
                        p = states.get(p).link;
                    }
                }
            }
            
            last = cur;
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
    
    /**
     * 3. SPOJ NSUBSTR - Substrings（统计不同长度子串的最大出现次数）
     * 题目描述：对于长度为x的子串，定义F(x)为该字符串中长度为x的子串的最大出现次数，输出F(1)..F(n)
     * 来源：https://www.spoj.com/problems/NSUBSTR/
     * 
     * 解题思路：
     * 1. 构建后缀自动机
     * 2. 对每个状态，用endposSize更新对应长度的计数
     * 3. 由于parent树的性质，需要从叶子节点向根节点更新
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
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
    
    /**
     * 4. SPOJ SUBLEX - Lexicographical Substring Search（字典序第K小子串）
     * 题目描述：给定一个字符串，找出其字典序第K小的子串
     * 来源：https://www.spoj.com/problems/SUBLEX/
     * 
     * 解题思路：
     * 1. 构建后缀自动机
     * 2. 预处理每个状态能形成的子串数量
     * 3. 按字典序DFS查找第K小子串
     * 
     * 时间复杂度：O(n + k)，其中k是查询次数
     * 空间复杂度：O(n)
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
    
    /**
     * 5. Codeforces 271D - Good Substrings（好子串计数）
     * 题目描述：给定一个字符串和一个标记字符串，标记字符串中'1'表示对应字符是好字符
     * 一个子串是好子串当且仅当其中坏字符的数量不超过k个
     * 来源：https://codeforces.com/problemset/problem/271/D
     * 
     * 解题思路：
     * 1. 构建后缀自动机
     * 2. 在自动机上DP，状态为(节点, 坏字符数量)
     * 3. 统计所有满足条件的路径
     * 
     * 时间复杂度：O(n * k * 26)
     * 空间复杂度：O(n * k)
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
    
    /**
     * 6. Codeforces 432D - Prefixes and Suffixes（前缀与后缀）
     * 题目描述：给定一个字符串，找出所有既是前缀又是后缀的子串，以及它们的出现次数
     * 来源：https://codeforces.com/problemset/problem/432/D
     * 
     * 解题思路：
     * 1. 构建后缀自动机
     * 2. 找出所有既是前缀又是后缀的子串（即从初始状态可以到达且是终止状态的节点）
     * 3. 计算每个子串的出现次数
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
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
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试1: SPOJ LCS - Longest Common Substring
        System.out.println("=== 测试1: SPOJ LCS - Longest Common Substring ===");
        String s1 = "abcdef";
        String s2 = "zabcxy";
        int lcsLength = longestCommonSubstring(s1, s2);
        System.out.println("字符串1: " + s1);
        System.out.println("字符串2: " + s2);
        System.out.println("最长公共子串长度: " + lcsLength); // 应该是3 ("abc")
        
        // 测试2: SPOJ NSUBSTR - Substrings
        System.out.println("\n=== 测试2: SPOJ NSUBSTR - Substrings ===");
        String s3 = "ababa";
        int[] counts = countSubstringsByLength(s3);
        System.out.println("字符串: " + s3);
        System.out.println("不同长度子串的最大出现次数:");
        for (int i = 1; i <= s3.length(); i++) {
            System.out.println("F(" + i + ") = " + counts[i]);
        }
        
        // 测试3: Codeforces 432D - Prefixes and Suffixes
        System.out.println("\n=== 测试3: Codeforces 432D - Prefixes and Suffixes ===");
        String s4 = "ababab";
        List<Map.Entry<String, Integer>> prefixes = prefixesAndSuffixes(s4);
        System.out.println("字符串: " + s4);
        System.out.println("既是前缀又是后缀的子串及其出现次数:");
        for (Map.Entry<String, Integer> entry : prefixes) {
            System.out.println("\"" + entry.getKey() + "\": " + entry.getValue() + "次");
        }
        
        // 测试4: SPOJ SUBLEX - Lexicographical Substring Search
        System.out.println("\n=== 测试4: SPOJ SUBLEX - Lexicographical Substring Search ===");
        String s5 = "abc";
        System.out.println("字符串: " + s5);
        for (int k = 1; k <= 5; k++) {
            String kth = kthSubstring(s5, k);
            System.out.println("第" + k + "小的子串: \"" + kth + "\"");
        }
    }
}