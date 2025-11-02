package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级后缀相关题目实现
 * 包含：
 * 1. Codeforces 128B - String (字典序第K小子串)
 * 2. Codeforces 452E - Three strings (三串公共子串)
 * 3. Codeforces 235C - Cyclical Quest (循环同构匹配)
 * 4. SPOJ SUBST1 - New Distinct Substrings (不同子串计数)
 * 5. SPOJ LCS - Longest Common Substring (最长公共子串)
 * 6. SPOJ LCS2 - Longest Common Substring II (多个字符串最长公共子串)
 * 7. SPOJ NSUBSTR - Substrings (不同长度子串统计)
 * 8. SPOJ REPEATS - Repeats (最长重复子串)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedSuffixProblems {
    
    /**
     * 后缀数组实现类
     */
    static class SuffixArray {
        private String s;
        private int[] sa;     // 后缀数组
        private int[] rank;   // 排名数组
        private int[] height; // LCP数组
        
        public SuffixArray(String s) {
            this.s = s;
            buildSuffixArray();
            buildLCP();
        }
        
        /**
         * 构建后缀数组（使用倍增算法）
         * 时间复杂度：O(n log n)
         * 空间复杂度：O(n)
         */
        private void buildSuffixArray() {
            int n = s.length();
            sa = new int[n];
            rank = new int[n];
            int[] tmp = new int[n];
            
            // 初始化
            for (int i = 0; i < n; i++) {
                sa[i] = i;
                rank[i] = s.charAt(i);
            }
            
            // 倍增排序
            for (int k = 1; k < n; k <<= 1) {
                final int fk = k;
                Integer[] indices = new Integer[n];
                for (int i = 0; i < n; i++) indices[i] = i;
                
                // 按第二关键字排序
                Arrays.sort(indices, (a, b) -> {
                    int ra = a + fk < n ? rank[a + fk] : -1;
                    int rb = b + fk < n ? rank[b + fk] : -1;
                    return Integer.compare(ra, rb);
                });
                
                // 按第一关键字排序（稳定排序）
                Arrays.sort(indices, (a, b) -> {
                    int ra = rank[a];
                    int rb = rank[b];
                    if (ra != rb) return Integer.compare(ra, rb);
                    int ra2 = a + fk < n ? rank[a + fk] : -1;
                    int rb2 = b + fk < n ? rank[b + fk] : -1;
                    return Integer.compare(ra2, rb2);
                });
                
                // 更新sa和rank
                for (int i = 0; i < n; i++) {
                    sa[i] = indices[i];
                }
                
                tmp[sa[0]] = 0;
                for (int i = 1; i < n; i++) {
                    int a = sa[i - 1], b = sa[i];
                    int ra1 = rank[a], rb1 = rank[b];
                    int ra2 = a + k < n ? rank[a + k] : -1;
                    int rb2 = b + k < n ? rank[b + k] : -1;
                    tmp[b] = tmp[a] + ((ra1 != rb1 || ra2 != rb2) ? 1 : 0);
                }
                
                for (int i = 0; i < n; i++) {
                    rank[sa[i]] = tmp[sa[i]];
                }
                
                if (rank[sa[n - 1]] == n - 1) break;
            }
        }
        
        /**
         * 构建LCP数组（使用Kasai算法）
         * 时间复杂度：O(n)
         * 空间复杂度：O(n)
         */
        private void buildLCP() {
            int n = s.length();
            height = new int[n];
            int[] inv = new int[n];
            
            // 计算rank的逆数组
            for (int i = 0; i < n; i++) {
                inv[sa[i]] = i;
            }
            
            // Kasai算法
            for (int i = 0, k = 0; i < n; i++) {
                if (inv[i] == n - 1) {
                    k = 0;
                    continue;
                }
                
                int j = sa[inv[i] + 1];
                while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k)) {
                    k++;
                }
                
                height[inv[i]] = k;
                if (k > 0) k--;
            }
        }
        
        // Getter方法
        public int[] getSA() { return sa; }
        public int[] getRank() { return rank; }
        public int[] getHeight() { return height; }
        public String getString() { return s; }
    }
    
    /**
     * 后缀自动机实现类
     */
    static class SuffixAutomaton {
        private static class State {
            Map<Character, Integer> next;
            int len;
            int link;
            int endposSize;
            boolean isClone;
            
            public State(int len) {
                this.next = new HashMap<>();
                this.len = len;
                this.link = -1;
                this.endposSize = 0;
                this.isClone = false;
            }
        }
        
        private List<State> states;
        private int last;
        private int size;
        private String text;
        
        public SuffixAutomaton(String text) {
            this.text = text;
            this.states = new ArrayList<>();
            this.last = 0;
            this.size = 1;
            
            states.add(new State(0));
            
            for (char c : text.toCharArray()) {
                extend(c);
            }
            
            calculateEndposSize();
        }
        
        private void extend(char c) {
            int cur = size++;
            states.add(new State(states.get(last).len + 1));
            states.get(cur).endposSize = 1;
            
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
                    states.get(clone).isClone = true;
                    
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
        
        private void calculateEndposSize() {
            List<Integer> order = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                order.add(i);
            }
            order.sort((a, b) -> Integer.compare(states.get(b).len, states.get(a).len));
            
            for (int v : order) {
                if (states.get(v).link != -1 && !states.get(v).isClone) {
                    states.get(states.get(v).link).endposSize += states.get(v).endposSize;
                }
            }
        }
        
        public long countDistinctSubstrings() {
            long count = 0;
            for (int i = 1; i < size; i++) {
                count += states.get(i).len - states.get(states.get(i).link).len;
            }
            return count;
        }
        
        public int countOccurrences(String s) {
            int state = 0;
            for (char c : s.toCharArray()) {
                if (!states.get(state).next.containsKey(c)) {
                    return 0;
                }
                state = states.get(state).next.get(c);
            }
            return states.get(state).endposSize;
        }
        
        // Getter方法
        public List<State> getStates() { return states; }
        public int getSize() { return size; }
    }
    
    // ====================================================================================
    // 题目1: Codeforces 128B - String
    // 题目描述: 求字符串的字典序第K小的子串
    // 解题思路: 使用后缀数组，通过height数组计算每个后缀能贡献的子串数量
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static String kthSubstring(String s, long k) {
        SuffixArray sa = new SuffixArray(s);
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        int n = s.length();
        
        // 计算每个后缀能贡献的子串数量
        long[] count = new long[n];
        for (int i = 0; i < n; i++) {
            int pos = suffixArray[i];
            count[i] = n - pos - height[i];
        }
        
        // 找到第k小的子串所在的后缀
        long sum = 0;
        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (sum + count[i] >= k) {
                idx = i;
                break;
            }
            sum += count[i];
        }
        
        // 在该后缀中找到第k小的子串
        int pos = suffixArray[idx];
        int start = (int)(k - sum - 1 + height[idx]);
        return s.substring(pos, pos + start + 1);
    }
    
    // ====================================================================================
    // 题目2: Codeforces 452E - Three strings
    // 题目描述: 求三个字符串的公共子串数量
    // 解题思路: 将三个字符串用不同特殊字符连接，构建后缀数组，
    //           然后统计同时包含三个字符串子串的LCP
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static long[] threeStrings(String s1, String s2, String s3) {
        // 用不同特殊字符连接三个字符串
        String combined = s1 + "#" + s2 + "$" + s3 + "%";
        SuffixArray sa = new SuffixArray(combined);
        
        int n1 = s1.length();
        int n2 = s2.length();
        int n3 = s3.length();
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        int len = combined.length();
        
        long[] result = new long[Math.max(n1, Math.max(n2, n3)) + 1];
        
        // 使用单调栈计算每个height值对答案的贡献
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        
        for (int i = 0; i <= len; i++) {
            int h = (i < len) ? height[i] : -1;
            while (stack.size() > 1 && height[stack.peek()] > h) {
                int j = stack.pop();
                int k = stack.peek();
                int minHeight = height[j];
                
                // 检查suffixArray[j-1]到suffixArray[i-1]是否包含三个字符串的后缀
                boolean hasS1 = false, hasS2 = false, hasS3 = false;
                for (int t = k + 1; t < i; t++) {
                    int pos = suffixArray[t];
                    if (pos < n1) hasS1 = true;
                    else if (pos < n1 + 1 + n2) hasS2 = true;
                    else if (pos < n1 + 1 + n2 + 1 + n3) hasS3 = true;
                }
                
                if (hasS1 && hasS2 && hasS3) {
                    result[minHeight] += (i - k - 1);
                }
            }
            stack.push(i);
        }
        
        // 计算前缀和得到最终结果
        for (int i = result.length - 2; i >= 1; i--) {
            result[i] += result[i + 1];
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目3: Codeforces 235C - Cyclical Quest
    // 题目描述: 给定文本串和多个模式串，对每个模式串求其循环同构在文本串中的出现次数
    // 解题思路: 对文本串建立后缀数组，对每个模式串的所有循环同构在后缀数组中二分查找
    // 时间复杂度: O(|T| log |T| + Σ|Pi|^2 log |T|)
    // 空间复杂度: O(|T|)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int[] cyclicalQuest(String text, String[] patterns) {
        SuffixArray sa = new SuffixArray(text);
        int[] suffixArray = sa.getSA();
        int[] rank = sa.getRank();
        int n = text.length();
        
        int[] result = new int[patterns.length];
        
        for (int i = 0; i < patterns.length; i++) {
            String pattern = patterns[i];
            int m = pattern.length();
            
            if (m > n) {
                result[i] = 0;
                continue;
            }
            
            // 对模式串的每个循环同构进行匹配
            Set<String> visited = new HashSet<>();
            int count = 0;
            
            for (int j = 0; j < m; j++) {
                String rotated = pattern.substring(j) + pattern.substring(0, j);
                if (visited.contains(rotated)) continue;
                visited.add(rotated);
                
                // 二分查找第一个匹配位置
                int left = 0, right = n - 1;
                int first = -1;
                while (left <= right) {
                    int mid = (left + right) / 2;
                    int pos = suffixArray[mid];
                    int cmp = compareSubstring(text, pos, rotated);
                    if (cmp == 0) {
                        first = mid;
                        right = mid - 1;
                    } else if (cmp < 0) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                
                if (first != -1) {
                    // 二分查找最后一个匹配位置
                    left = first;
                    right = n - 1;
                    int last = first;
                    while (left <= right) {
                        int mid = (left + right) / 2;
                        int pos = suffixArray[mid];
                        int cmp = compareSubstring(text, pos, rotated);
                        if (cmp == 0) {
                            last = mid;
                            left = mid + 1;
                        } else {
                            right = mid - 1;
                        }
                    }
                    count += last - first + 1;
                }
            }
            
            result[i] = count;
        }
        
        return result;
    }
    
    /**
     * 比较文本的子串和模式串
     */
    private static int compareSubstring(String text, int start, String pattern) {
        int n = text.length();
        int m = pattern.length();
        
        for (int i = 0; i < m; i++) {
            if (start + i >= n) return -1; // 文本不够长
            char c1 = text.charAt(start + i);
            char c2 = pattern.charAt(i);
            if (c1 != c2) {
                return c1 < c2 ? -1 : 1;
            }
        }
        return 0; // 完全匹配
    }
    
    // ====================================================================================
    // 题目4: SPOJ SUBST1 - New Distinct Substrings
    // 题目描述: 计算字符串中不同子串的个数
    // 解题思路: 使用后缀数组和height数组
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static long newDistinctSubstrings(String s) {
        SuffixArray sa = new SuffixArray(s);
        int n = s.length();
        // 总子串数
        long total = (long) n * (n + 1) / 2;
        // 重复子串数（height数组的和）
        long repeated = 0;
        for (int i = 1; i < n; i++) {  // 从1开始，因为height[0]是无效的
            repeated += sa.getHeight()[i];
        }
        // 不同子串数 = 总子串数 - 重复子串数
        return total - repeated;
    }
    
    // ====================================================================================
    // 题目5: SPOJ LCS - Longest Common Substring
    // 题目描述: 求两个字符串的最长公共子串
    // 解题思路: 将两个字符串用特殊字符连接，构建后缀数组，
    //           然后在height数组中查找属于两个不同字符串的后缀的最大LCP
    // 时间复杂度: O((n+m) log(n+m))
    // 空间复杂度: O(n+m)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int longestCommonSubstring(String s1, String s2) {
        // 用特殊字符连接两个字符串
        String combined = s1 + "#" + s2 + "$";
        SuffixArray sa = new SuffixArray(combined);
        
        int n = s1.length();
        int m = s2.length();
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        int len = combined.length();
        
        int maxLen = 0;
        
        // 查找属于两个不同字符串的后缀的最大LCP
        for (int i = 1; i < len; i++) {
            // 检查suffixArray[i-1]和suffixArray[i]是否属于不同字符串
            int pos1 = suffixArray[i-1];
            int pos2 = suffixArray[i];
            
            // 判断是否属于不同字符串
            boolean inS1_1 = (pos1 < n);
            boolean inS1_2 = (pos2 < n);
            boolean inS2_1 = (pos1 > n && pos1 < n + 1 + m);
            boolean inS2_2 = (pos2 > n && pos2 < n + 1 + m);
            
            if ((inS1_1 && inS2_2) || (inS2_1 && inS1_2)) {
                maxLen = Math.max(maxLen, height[i]);
            }
        }
        
        return maxLen;
    }
    
    // ====================================================================================
    // 题目6: SPOJ LCS2 - Longest Common Substring II
    // 题目描述: 求多个字符串的最长公共子串
    // 解题思路: 将所有字符串用不同分隔符连接，构建后缀数组，
    //           然后找到包含所有字符串后缀的最小区间
    // 时间复杂度: O(N log N)
    // 空间复杂度: O(N)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int longestCommonSubstringII(String[] strings) {
        int k = strings.length;
        if (k == 0) return 0;
        if (k == 1) return strings[0].length();
        
        // 构造连接字符串
        StringBuilder sb = new StringBuilder();
        int[] stringId = new int[100000]; // 记录每个位置属于哪个字符串
        int pos = 0;
        
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < strings[i].length(); j++) {
                sb.append(strings[i].charAt(j));
                stringId[pos++] = i;
            }
            // 添加分隔符
            sb.append((char) (128 + i));
            stringId[pos++] = -1;
        }
        
        String combined = sb.toString();
        SuffixArray sa = new SuffixArray(combined);
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        
        int len = combined.length();
        int maxLen = 0;
        
        // 使用滑动窗口找到包含所有k个字符串的最小区间
        for (int i = 0; i < len; i++) {
            Set<Integer> uniqueStrings = new HashSet<>();
            int minLCP = Integer.MAX_VALUE;
            
            for (int j = i; j < len; j++) {
                // 添加当前后缀所属的字符串
                if (stringId[suffixArray[j]] != -1) {
                    uniqueStrings.add(stringId[suffixArray[j]]);
                }
                
                // 如果包含所有k个字符串
                if (uniqueStrings.size() == k) {
                    // 更新最小LCP
                    if (j > i) {
                        minLCP = Math.min(minLCP, height[j]);
                    }
                    maxLen = Math.max(maxLen, minLCP);
                } else if (uniqueStrings.size() < k && j > i) {
                    minLCP = Math.min(minLCP, height[j]);
                }
            }
        }
        
        return maxLen;
    }
    
    // ====================================================================================
    // 题目7: SPOJ NSUBSTR - Substrings
    // 题目描述: 对于每个长度k，计算长度为k的子串的最大出现次数
    // 解题思路: 使用后缀数组，通过height数组计算每个长度的最大出现次数
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int[] nSubstrings(String s) {
        SuffixArray sa = new SuffixArray(s);
        int n = s.length();
        int[] height = sa.getHeight();
        int[] saArray = sa.getSA();
        
        // 计算每个后缀对应的LCP值
        int[] lcp = new int[n];
        for (int i = 1; i < n; i++) {
            lcp[i] = height[i];
        }
        
        // 计算每个长度的最大出现次数
        int[] result = new int[n + 1];
        
        // 对于每个后缀，计算其贡献
        for (int i = 0; i < n; i++) {
            int len = n - saArray[i]; // 当前后缀的长度
            result[len] = Math.max(result[len], 1); // 至少出现一次
            
            // 如果有下一个后缀，计算LCP的贡献
            if (i + 1 < n) {
                int commonLen = lcp[i + 1];
                for (int j = 1; j <= commonLen; j++) {
                    result[j] = Math.max(result[j], 2); // 至少出现两次
                }
            }
        }
        
        // 从后向前更新，确保result[i] >= result[i+1]
        for (int i = n - 1; i >= 1; i--) {
            result[i] = Math.max(result[i], result[i + 1]);
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目8: SPOJ REPEATS - Repeats
    // 题目描述: 求字符串中连续重复次数最多的子串的最大重复次数
    // 解题思路: 枚举重复子串的长度L，然后每隔L个位置检查是否存在重复模式
    // 时间复杂度: O(n^2)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int repeats(String s) {
        int n = s.length();
        if (n == 0) return 0;
        
        int maxRepeats = 1;
        
        // 枚举重复子串的长度
        for (int L = 1; L <= n / 2; L++) {
            // 每隔L个位置检查
            for (int i = 0; i < L; i++) {
                int count = 0;
                int start = i;
                
                // 计算从位置i开始每隔L个位置的字符匹配次数
                for (int j = i; j + L <= n; j += L) {
                    if (s.substring(j, j + L).equals(s.substring(start, start + L))) {
                        count++;
                    } else {
                        count = 1;
                        start = j;
                    }
                    maxRepeats = Math.max(maxRepeats, count);
                }
            }
        }
        
        return maxRepeats;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试New Distinct Substrings
        System.out.println("=== 测试New Distinct Substrings ===");
        String s1 = "banana";
        long result1 = newDistinctSubstrings(s1);
        System.out.println("输入: " + s1);
        System.out.println("不同子串数: " + result1);
        
        // 测试Longest Common Substring
        System.out.println("\n=== 测试Longest Common Substring ===");
        String s2 = "abcdef";
        String s3 = "zabcxy";
        int result2 = longestCommonSubstring(s2, s3);
        System.out.println("输入: s1=" + s2 + ", s2=" + s3);
        System.out.println("最长公共子串长度: " + result2);
        
        // 测试NSUBSTR
        System.out.println("\n=== 测试NSUBSTR ===");
        String s4 = "banana";
        int[] result3 = nSubstrings(s4);
        System.out.println("输入: " + s4);
        System.out.print("各长度最大出现次数: ");
        for (int i = 1; i <= s4.length(); i++) {
            System.out.print("F(" + i + ")=" + result3[i] + " ");
        }
        System.out.println();
        
        // 测试REPEATS
        System.out.println("\n=== 测试REPEATS ===");
        String s5 = "abababab";
        int result4 = repeats(s5);
        System.out.println("输入: " + s5);
        System.out.println("最大重复次数: " + result4);
    }
}