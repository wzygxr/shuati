package class029_AdvancedDataStructures;

import java.util.*;

public class TestSuffixAutomaton {
    public static void main(String[] args) {
        System.out.println("测试后缀自动机相关题目实现");
        
        // 简单测试后缀自动机基本功能
        SuffixAutomaton sam = new SuffixAutomaton("banana");
        System.out.println("文本: banana");
        System.out.println("不同子串数量: " + sam.countDistinctSubstrings());
        System.out.println("包含 'ana': " + sam.contains("ana"));
        System.out.println("包含 'xyz': " + sam.contains("xyz"));
        System.out.println("'a' 出现次数: " + sam.countOccurrences("a"));
        System.out.println("'na' 出现次数: " + sam.countOccurrences("na"));
        System.out.println("最长重复子串: " + sam.findLongestRepeatedSubstring());
    }
    
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

        public State(int len) {
            this.next = new HashMap<>();
            this.len = len;
            this.link = -1;
            this.endposSize = 0;
            this.isClone = false;
            this.firstPos = 0;
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
}