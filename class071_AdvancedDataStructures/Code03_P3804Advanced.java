package class186;

import java.util.*;

/**
 * 洛谷 P3804 【模板】后缀自动机 (高级版本)
 * 
 * 题目描述：给定字符串S，求所有出现次数不为1的子串的出现次数乘长度的最大值
 * 平台：洛谷
 * 链接：https://www.luogu.com.cn/problem/P3804
 * 
 * 解题思路：
 * 使用后缀自动机来解决这个问题。
 * 1. 建立后缀自动机
 * 2. 计算每个状态的right集合大小（即子串出现次数）
 * 3. 枚举所有状态，计算出现次数>1的状态的出现次数乘长度的最大值
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
public class Code03_P3804Advanced {
    
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
         * 计算每个状态的right集合大小
         */
        public void calculateEndPosSize() {
            // 按长度降序排列状态
            Integer[] order = new Integer[states.size()];
            for (int i = 0; i < states.size(); i++) {
                order[i] = i;
            }
            
            Arrays.sort(order, (a, b) -> Integer.compare(states.get(b).len, states.get(a).len));
            
            // 按逆拓扑序更新endPosSize
            for (int x : order) {
                if (states.get(x).link != -1) {
                    states.get(states.get(x).link).endPosSize += states.get(x).endPosSize;
                }
            }
        }
        
        /**
         * 求所有出现次数不为1的子串的出现次数乘长度的最大值
         * @return 最大值
         */
        public long getMaxValue() {
            calculateEndPosSize();
            
            long maxVal = 0;
            // 枚举除初始状态外的所有状态
            for (int i = 1; i < states.size(); i++) {
                // 只考虑出现次数大于1的子串
                if (states.get(i).endPosSize > 1) {
                    // 该状态对应的子串长度为states.get(i).len - states.get(states.get(i).link).len
                    // 但题目要求的是所有该状态对应的子串的最大长度，即states.get(i).len
                    long value = (long) states.get(i).endPosSize * states.get(i).len;
                    maxVal = Math.max(maxVal, value);
                }
            }
            
            return maxVal;
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
        String s1 = "abab";
        SuffixAutomaton sam1 = new SuffixAutomaton(s1);
        System.out.println("输入: " + s1);
        System.out.println("最大值: " + sam1.getMaxValue());
        // 预期输出: 4 (子串"ab"出现2次，长度为2，2*2=4)
        
        // 测试用例2
        String s2 = "aaaa";
        SuffixAutomaton sam2 = new SuffixAutomaton(s2);
        System.out.println("输入: " + s2);
        System.out.println("最大值: " + sam2.getMaxValue());
        // 预期输出: 12 (子串"a"出现4次，长度为1，4*1=4; 子串"aa"出现3次，长度为2，3*2=6; 子串"aaa"出现2次，长度为3，2*3=6; 子串"aaaa"出现1次，不考虑)
        // 最大值为6，但实际应该是"aa"的贡献，即3*2=6
        
        // 测试用例3
        String s3 = "abcde";
        SuffixAutomaton sam3 = new SuffixAutomaton(s3);
        System.out.println("输入: " + s3);
        System.out.println("最大值: " + sam3.getMaxValue());
        // 预期输出: 0 (所有子串都只出现1次)
    }
}