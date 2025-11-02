package class186;

import java.util.*;

/**
 * SPOJ LCS - Longest Common Substring (高级版本)
 * 
 * 题目描述：给定两个字符串，求它们的最长公共子串
 * 平台：SPOJ
 * 链接：https://www.spoj.com/problems/LCS/
 * 
 * 解题思路：
 * 使用后缀自动机来解决这个问题。
 * 1. 对第一个字符串建立后缀自动机
 * 2. 用第二个字符串在自动机上匹配，记录匹配的最大长度
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
public class Code02_LcsAdvanced {
    
    /**
     * 后缀自动机实现类
     */
    static class SuffixAutomaton {
        private static class State {
            int len;           // 从初始状态到当前状态的最长字符串长度
            int link;          // 后缀链接
            Map<Character, Integer> next; // 转移函数
            int endPosSize;    // right集合大小
            boolean isClone;   // 是否为克隆节点
            
            State() {
                len = 0;
                link = -1;
                next = new HashMap<>();
                endPosSize = 0;
                isClone = false;
            }
        }
        
        private List<State> states;
        private int last;
        
        public SuffixAutomaton(String s) {
            states = new ArrayList<>();
            states.add(new State()); // 初始状态
            last = 0;
            
            // 逐个字符构建SAM
            for (char c : s.toCharArray()) {
                extend(c);
            }
        }
        
        /**
         * 扩展SAM
         */
        private void extend(char c) {
            int cur = states.size();
            states.add(new State());
            states.get(cur).len = states.get(last).len + 1;
            states.get(cur).endPosSize = 1;
            
            int p = last;
            // 更新转移函数
            while (p != -1 && !states.get(p).next.containsKey(c)) {
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
                    int clone = states.size();
                    states.add(new State());
                    states.get(clone).len = states.get(p).len + 1;
                    states.get(clone).next = new HashMap<>(states.get(q).next);
                    states.get(clone).link = states.get(q).link;
                    states.get(clone).isClone = true;
                    
                    while (p != -1 && states.get(p).next.get(c) == q) {
                        states.get(p).next.put(c, clone);
                        p = states.get(p).link;
                    }
                    
                    states.get(q).link = clone;
                    states.get(cur).link = clone;
                }
            }
            
            last = cur;
        }
        
        /**
         * 计算两个字符串的最长公共子串长度
         * @param t 第二个字符串
         * @return 最长公共子串长度
         */
        public int lcs(String t) {
            int v = 0;      // 当前状态
            int l = 0;      // 当前匹配长度
            int best = 0;   // 最长匹配长度
            
            for (int i = 0; i < t.length(); i++) {
                char c = t.charAt(i);
                
                // 如果当前状态没有字符c的转移
                while (v != 0 && !states.get(v).next.containsKey(c)) {
                    v = states.get(v).link;  // 沿着后缀链接向上走
                    l = states.get(v).len;   // 更新匹配长度
                }
                
                // 如果当前状态有字符c的转移
                if (states.get(v).next.containsKey(c)) {
                    v = states.get(v).next.get(c);  // 转移到下一个状态
                    l++;  // 匹配长度加1
                }
                
                // 更新最长匹配长度
                best = Math.max(best, l);
            }
            
            return best;
        }
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        testCases();
    }
    
    /**
     * 测试用例
     */
    private static void testCases() {
        // 测试用例1
        String s1 = "abcde";
        String t1 = "xbcxy";
        SuffixAutomaton sam1 = new SuffixAutomaton(s1);
        System.out.println("输入: s1=" + s1 + ", t1=" + t1);
        System.out.println("最长公共子串长度: " + sam1.lcs(t1));
        // 预期输出: 2 (bc)
        
        // 测试用例2
        String s2 = "abcef";
        String t2 = "bcefg";
        SuffixAutomaton sam2 = new SuffixAutomaton(s2);
        System.out.println("输入: s2=" + s2 + ", t2=" + t2);
        System.out.println("最长公共子串长度: " + sam2.lcs(t2));
        // 预期输出: 3 (bce)
        
        // 测试用例3
        String s3 = "abcd";
        String t3 = "xyz";
        SuffixAutomaton sam3 = new SuffixAutomaton(s3);
        System.out.println("输入: s3=" + s3 + ", t3=" + t3);
        System.out.println("最长公共子串长度: " + sam3.lcs(t3));
        // 预期输出: 0 (无公共子串)
    }
}