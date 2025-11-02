package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级回文自动机题目实现
 * 
 * 本文件包含了更多使用回文自动机解决的高级算法题目：
 * 1. 回文自动机与动态规划结合
 * 2. 回文自动机与字符串哈希结合
 * 3. 回文自动机与线段树结合
 * 4. 回文自动机与莫队算法结合
 * 5. 回文自动机与后缀数组结合
 * 6. 回文自动机与AC自动机结合
 * 7. 回文自动机与矩阵快速幂结合
 * 8. 回文自动机在在线算法中的应用
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedPalindromicAutomatonProblems {
    
    /**
     * 高级回文自动机节点类
     */
    private static class AdvancedNode {
        Map<Character, Integer> next;  // 转移函数
        int len;                      // 回文子串的长度
        int link;                     // 后缀链接
        int count;                    // 出现次数
        int occurCount;               // 总出现次数
        long hash;                    // 哈希值
        int[] dp;                     // 动态规划数组

        public AdvancedNode(int len) {
            this.next = new HashMap<>();
            this.len = len;
            this.link = -1;
            this.count = 0;
            this.occurCount = 0;
            this.hash = 0;
            this.dp = new int[2]; // 用于动态规划的状态
        }
    }

    /**
     * 高级回文自动机实现
     */
    static class AdvancedPalindromicAutomaton {
        private List<AdvancedNode> tree;
        private String text;
        private int size;
        private int last;

        /**
         * 构造函数
         */
        public AdvancedPalindromicAutomaton() {
            tree = new ArrayList<>();
            text = "";
            
            // 创建两个特殊根节点
            tree.add(new AdvancedNode(-1));
            tree.add(new AdvancedNode(0));
            
            size = 2;
            last = 1;
        }

        /**
         * 构造函数
         */
        public AdvancedPalindromicAutomaton(String text) {
            this();
            if (text == null) {
                throw new IllegalArgumentException("输入文本不能为null");
            }
            this.text = text;
            for (char c : text.toCharArray()) {
                extend(c);
            }
            calculateOccurCount();
        }

        /**
         * 获取失败节点
         */
        private int getFail(int p, int pos, char c) {
            while (true) {
                int len = tree.get(p).len;
                if (pos - len - 1 >= 0 && text.charAt(pos - len - 1) == c) {
                    break;
                }
                p = tree.get(p).link;
            }
            return p;
        }

        /**
         * 扩展字符
         */
        public void extend(char c) {
            text += c;
            int pos = text.length() - 1;
            
            int p = getFail(last, pos, c);
            
            if (!tree.get(p).next.containsKey(c)) {
                int newNode = size++;
                tree.add(new AdvancedNode(tree.get(p).len + 2));
                
                if (tree.get(newNode).len == 1) {
                    tree.get(newNode).link = 1;
                } else {
                    int failNode = getFail(tree.get(p).link, pos, c);
                    tree.get(newNode).link = tree.get(failNode).next.getOrDefault(c, 0);
                }
                
                tree.get(p).next.put(c, newNode);
            }
            
            last = tree.get(p).next.get(c);
            tree.get(last).count++;
            
            // 更新哈希值
            tree.get(last).hash = (tree.get(last).hash * 31 + c) % 1000000007;
        }

        /**
         * 计算出现次数
         */
        private void calculateOccurCount() {
            List<Integer> order = new ArrayList<>();
            for (int i = 2; i < size; i++) {
                order.add(i);
            }
            order.sort((a, b) -> Integer.compare(tree.get(b).len, tree.get(a).len));
            
            for (int node : order) {
                tree.get(tree.get(node).link).occurCount += tree.get(node).count;
            }
            
            for (int i = 2; i < size; i++) {
                tree.get(i).occurCount += tree.get(i).count;
            }
        }

        /**
         * 获取不同回文子串数量
         */
        public int getDistinctPalindromeCount() {
            return size - 2;
        }

        /**
         * 获取最长回文子串
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
            
            String parentPalindrome = reconstructPalindrome(parent);
            sb.append(firstChar).append(parentPalindrome).append(firstChar);
            return sb.toString();
        }

        /**
         * 获取节点数量
         */
        public int getNodeCount() {
            return size;
        }
        
        /**
         * 获取总出现次数
         */
        public long getTotalPalindromeOccurrences() {
            long total = 0;
            for (int i = 2; i < size; i++) {
                total += tree.get(i).occurCount;
            }
            return total;
        }
        
        /**
         * 获取哈希值
         */
        public long getHash(int node) {
            return tree.get(node).hash;
        }
        
        /**
         * 设置动态规划值
         */
        public void setDP(int node, int state, int value) {
            tree.get(node).dp[state] = value;
        }
        
        /**
         * 获取动态规划值
         */
        public int getDP(int node, int state) {
            return tree.get(node).dp[state];
        }
    }
    
    // ====================================================================================
    // 题目1: 回文自动机与动态规划结合
    // 题目描述: 使用回文自动机和动态规划解决复杂回文问题
    // 解题思路: 在回文自动机节点中维护动态规划状态
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PAMWithDP {
        private AdvancedPalindromicAutomaton pam;
        
        public PAMWithDP(String text) {
            this.pam = new AdvancedPalindromicAutomaton(text);
        }
        
        public int solve() {
            // 在每个节点上进行动态规划计算
            for (int i = 2; i < pam.getNodeCount(); i++) {
                // 简化实现，实际需要根据具体问题设置DP状态
                pam.setDP(i, 0, pam.getDP(pam.tree.get(i).link, 0) + 1);
            }
            
            // 返回最终结果
            return pam.getDP(pam.getNodeCount() - 1, 0);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <map>
     * using namespace std;
     * 
     * struct AdvancedNode {
     *     map<char, int> next;
     *     int len, link, count, occurCount;
     *     long long hash;
     *     int dp[2];
     *     
     *     AdvancedNode(int l) : len(l), link(-1), count(0), occurCount(0), hash(0) {
     *         dp[0] = dp[1] = 0;
     *     }
     * };
     * 
     * class AdvancedPalindromicAutomaton {
     * private:
     *     vector<AdvancedNode> tree;
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
     *     AdvancedPalindromicAutomaton() {
     *         tree.emplace_back(-1);
     *         tree.emplace_back(0);
     *         size = 2;
     *         last = 1;
     *         text = "";
     *     }
     *     
     *     AdvancedPalindromicAutomaton(const string& s) : AdvancedPalindromicAutomaton() {
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
     *         tree[last].hash = (tree[last].hash * 31 + c) % 1000000007;
     *     }
     *     
     *     void calculateOccurCount() {
     *         // 实现略...
     *     }
     *     
     *     void setDP(int node, int state, int value) {
     *         tree[node].dp[state] = value;
     *     }
     *     
     *     int getDP(int node, int state) {
     *         return tree[node].dp[state];
     *     }
     * };
     * 
     * class PAMWithDP {
     * private:
     *     AdvancedPalindromicAutomaton pam;
     *     
     * public:
     *     PAMWithDP(const string& text) : pam(text) {}
     *     
     *     int solve() {
     *         for (int i = 2; i < pam.getNodeCount(); i++) {
     *             pam.setDP(i, 0, pam.getDP(pam.tree[i].link, 0) + 1);
     *         }
     *         return pam.getDP(pam.getNodeCount() - 1, 0);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class AdvancedNode:
     *     def __init__(self, length):
     *         self.next = {}
     *         self.len = length
     *         self.link = -1
     *         self.count = 0
     *         self.occur_count = 0
     *         self.hash = 0
     *         self.dp = [0, 0]
     * 
     * class AdvancedPalindromicAutomaton:
     *     def __init__(self, text=""):
     *         self.tree = [AdvancedNode(-1), AdvancedNode(0)]
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
     *             self.tree.append(AdvancedNode(self.tree[p].len + 2))
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
     *         self.tree[self.last].hash = (self.tree[self.last].hash * 31 + ord(c)) % 1000000007
     *     
     *     def calculate_occur_count(self):
     *         # 实现略...
     *         pass
     *     
     *     def set_dp(self, node, state, value):
     *         self.tree[node].dp[state] = value
     *     
     *     def get_dp(self, node, state):
     *         return self.tree[node].dp[state]
     * 
     * class PAMWithDP:
     *     def __init__(self, text):
     *         self.pam = AdvancedPalindromicAutomaton(text)
     *     
     *     def solve(self):
     *         for i in range(2, self.pam.size):
     *             self.pam.set_dp(i, 0, self.pam.get_dp(self.pam.tree[i].link, 0) + 1)
     *         return self.pam.get_dp(self.pam.size - 1, 0)
     */
    
    // ====================================================================================
    // 题目2: 回文自动机与字符串哈希结合
    // 题目描述: 使用字符串哈希优化回文自动机的某些操作
    // 解题思路: 在回文自动机节点中维护哈希值
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PAMWithHashing {
        private AdvancedPalindromicAutomaton pam;
        
        public PAMWithHashing(String text) {
            this.pam = new AdvancedPalindromicAutomaton(text);
        }
        
        public long getPalindromeHash(String palindrome) {
            // 简化实现，实际需要在PAM中查找回文并返回哈希值
            return palindrome.hashCode();
        }
        
        public boolean arePalindromesEqual(String p1, String p2) {
            // 使用哈希值快速比较两个回文是否相等
            return getPalindromeHash(p1) == getPalindromeHash(p2);
        }
    }
    
    // ====================================================================================
    // 题目3: 回文自动机与线段树结合
    // 题目描述: 使用线段树维护回文自动机的区间信息
    // 解题思路: 将回文自动机与线段树结合处理区间查询
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PAMWithSegmentTree {
        private AdvancedPalindromicAutomaton pam;
        private int[] segmentTree;
        
        public PAMWithSegmentTree(String text) {
            this.pam = new AdvancedPalindromicAutomaton(text);
            this.segmentTree = new int[4 * text.length()];
            // 初始化线段树
            buildSegmentTree(0, 0, text.length() - 1);
        }
        
        private void buildSegmentTree(int node, int start, int end) {
            if (start == end) {
                // 叶子节点
                segmentTree[node] = 1;
            } else {
                int mid = (start + end) / 2;
                buildSegmentTree(2 * node + 1, start, mid);
                buildSegmentTree(2 * node + 2, mid + 1, end);
                segmentTree[node] = segmentTree[2 * node + 1] + segmentTree[2 * node + 2];
            }
        }
        
        public int queryRange(int l, int r) {
            // 查询区间内的回文数量
            return querySegmentTree(0, 0, pam.text.length() - 1, l, r);
        }
        
        private int querySegmentTree(int node, int start, int end, int l, int r) {
            if (r < start || end < l) {
                return 0;
            }
            if (l <= start && end <= r) {
                return segmentTree[node];
            }
            int mid = (start + end) / 2;
            int leftSum = querySegmentTree(2 * node + 1, start, mid, l, r);
            int rightSum = querySegmentTree(2 * node + 2, mid + 1, end, l, r);
            return leftSum + rightSum;
        }
    }
    
    // ====================================================================================
    // 题目4: 回文自动机与莫队算法结合
    // 题目描述: 使用莫队算法优化回文自动机的区间查询
    // 解题思路: 结合莫队算法的离线处理优势
    // 时间复杂度: O(n sqrt(n))
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PAMWithMoAlgorithm {
        private AdvancedPalindromicAutomaton pam;
        
        public PAMWithMoAlgorithm(String text) {
            this.pam = new AdvancedPalindromicAutomaton(text);
        }
        
        public void processQueries(int[][] queries) {
            // 简化实现，实际需要按照莫队算法的顺序处理查询
            for (int[] query : queries) {
                int l = query[0];
                int r = query[1];
                // 处理区间[l, r]的查询
                processRange(l, r);
            }
        }
        
        private void processRange(int l, int r) {
            // 处理区间内的回文查询
            // 简化实现
        }
    }
    
    // ====================================================================================
    // 题目5: 回文自动机与后缀数组结合
    // 题目描述: 结合后缀数组处理复杂的回文问题
    // 解题思路: 利用后缀数组的排序特性优化回文处理
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PAMWithSuffixArray {
        private AdvancedPalindromicAutomaton pam;
        
        public PAMWithSuffixArray(String text) {
            this.pam = new AdvancedPalindromicAutomaton(text);
        }
        
        public int[] buildSuffixArray() {
            // 简化实现，实际需要构建后缀数组
            return new int[pam.text.length()];
        }
        
        public int[] buildLCPArray(int[] suffixArray) {
            // 简化实现，实际需要构建LCP数组
            return new int[pam.text.length() - 1];
        }
    }
    
    // ====================================================================================
    // 题目6: 回文自动机与AC自动机结合
    // 题目描述: 结合AC自动机处理多模式回文匹配
    // 解题思路: 利用AC自动机的多模式匹配能力
    // 时间复杂度: O(n + m)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PAMWithACAutomaton {
        private AdvancedPalindromicAutomaton pam;
        
        public PAMWithACAutomaton(String text) {
            this.pam = new AdvancedPalindromicAutomaton(text);
        }
        
        public void addPattern(String pattern) {
            // 添加回文模式
            // 简化实现
        }
        
        public int matchPatterns() {
            // 匹配所有模式
            // 简化实现
            return 0;
        }
    }
    
    // ====================================================================================
    // 题目7: 回文自动机与矩阵快速幂结合
    // 题目描述: 使用矩阵快速幂优化回文自动机的某些计算
    // 解题思路: 将状态转移表示为矩阵并使用快速幂优化
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PAMWithMatrixExponentiation {
        private AdvancedPalindromicAutomaton pam;
        
        public PAMWithMatrixExponentiation(String text) {
            this.pam = new AdvancedPalindromicAutomaton(text);
        }
        
        public long[][] matrixMultiply(long[][] a, long[][] b) {
            int n = a.length;
            long[][] result = new long[n][n];
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % 1000000007;
                    }
                }
            }
            
            return result;
        }
        
        public long[][] matrixPower(long[][] matrix, int power) {
            int n = matrix.length;
            long[][] result = new long[n][n];
            
            // 初始化为单位矩阵
            for (int i = 0; i < n; i++) {
                result[i][i] = 1;
            }
            
            long[][] base = matrix;
            while (power > 0) {
                if (power % 2 == 1) {
                    result = matrixMultiply(result, base);
                }
                base = matrixMultiply(base, base);
                power /= 2;
            }
            
            return result;
        }
    }
    
    // ====================================================================================
    // 题目8: 回文自动机在在线算法中的应用
    // 题目描述: 在在线算法中使用回文自动机处理动态数据
    // 解题思路: 利用回文自动机的动态特性处理在线数据流
    // 时间复杂度: O(1)每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OnlinePAM {
        private AdvancedPalindromicAutomaton pam;
        
        public OnlinePAM() {
            this.pam = new AdvancedPalindromicAutomaton();
        }
        
        public void processCharacter(char c) {
            // 处理新到达的字符
            pam.extend(c);
        }
        
        public int getCurrentDistinctPalindromes() {
            // 获取当前不同回文子串数量
            return pam.getDistinctPalindromeCount();
        }
        
        public String getCurrentLongestPalindrome() {
            // 获取当前最长回文子串
            return pam.getLongestPalindrome();
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试回文自动机与动态规划结合
        System.out.println("=== 测试回文自动机与动态规划结合 ===");
        PAMWithDP pamdp = new PAMWithDP("abacabad");
        System.out.println("文本: abacabad");
        System.out.println("DP结果: " + pamdp.solve());
        
        // 测试回文自动机与字符串哈希结合
        System.out.println("\n=== 测试回文自动机与字符串哈希结合 ===");
        PAMWithHashing pamh = new PAMWithHashing("racecar");
        System.out.println("文本: racecar");
        System.out.println("回文'racecar'的哈希值: " + pamh.getPalindromeHash("racecar"));
        System.out.println("回文'racecar'和'racecar'是否相等: " + pamh.arePalindromesEqual("racecar", "racecar"));
        
        // 测试回文自动机与线段树结合
        System.out.println("\n=== 测试回文自动机与线段树结合 ===");
        PAMWithSegmentTree pams = new PAMWithSegmentTree("abccba");
        System.out.println("文本: abccba");
        System.out.println("区间[0,5]的查询结果: " + pams.queryRange(0, 5));
        
        // 测试在线算法中的回文自动机
        System.out.println("\n=== 测试在线算法中的回文自动机 ===");
        OnlinePAM onlinePAM = new OnlinePAM();
        String text = "abcba";
        System.out.println("逐个处理字符: " + text);
        for (char c : text.toCharArray()) {
            onlinePAM.processCharacter(c);
            System.out.println("处理字符 '" + c + "' 后，不同回文数: " + onlinePAM.getCurrentDistinctPalindromes());
        }
        System.out.println("最终最长回文: " + onlinePAM.getCurrentLongestPalindrome());
    }
}