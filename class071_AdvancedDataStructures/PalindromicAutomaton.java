package class186;

import java.util.*;

/**
 * 回文自动机（Palindromic Automaton, PAM）实现
 * 也称为Eertree，是一种能够高效处理字符串中所有回文子串的数据结构
 * 主要特性：
 * 1. 每个节点表示一个唯一的回文子串
 * 2. 支持动态添加字符并维护回文子串信息
 * 3. 可以高效统计不同回文子串数量、每个回文子串出现次数等
 * 
 * 时间复杂度：构建O(n)，查询O(1)或O(n)
 * 空间复杂度：O(n)
 */
public class PalindromicAutomaton {
    /**
     * 回文自动机的节点
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
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试用例1：基本功能测试
        String text1 = "banana";
        PalindromicAutomaton pam1 = new PalindromicAutomaton(text1);
        System.out.println("=== 测试用例1 ===");
        System.out.println("文本: " + text1);
        System.out.println("节点数量: " + pam1.getNodeCount());
        System.out.println("不同回文子串数量: " + pam1.getDistinctPalindromeCount());
        System.out.println("最长回文子串: " + pam1.getLongestPalindrome());
        System.out.println("所有不同回文子串: " + pam1.getAllDistinctPalindromes());
        System.out.println("'ana'出现次数: " + pam1.getOccurrenceCount("ana"));
        
        // 测试用例2：边界情况
        String text2 = "aaa";
        PalindromicAutomaton pam2 = new PalindromicAutomaton(text2);
        System.out.println("\n=== 测试用例2 ===");
        System.out.println("文本: " + text2);
        System.out.println("不同回文子串数量: " + pam2.getDistinctPalindromeCount());
        System.out.println("最长回文子串: " + pam2.getLongestPalindrome());
        
        // 测试用例3：动态添加字符
        System.out.println("\n=== 测试用例3（动态添加）===");
        PalindromicAutomaton pam3 = new PalindromicAutomaton();
        String text3 = "mississippi";
        System.out.println("动态添加文本: " + text3);
        for (char c : text3.toCharArray()) {
            pam3.extend(c);
        }
        // 动态添加后需要重新计算出现次数
        pam3.calculateOccurCount();
        System.out.println("不同回文子串数量: " + pam3.getDistinctPalindromeCount());
        System.out.println("最长回文子串: " + pam3.getLongestPalindrome());
    }
}