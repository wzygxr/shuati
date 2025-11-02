package class044;

import java.util.*;
import java.io.*;

/*
 * 题目4: POJ 3630 / HDU 1671 Phone List
 * 题目来源：POJ / HDU
 * 题目链接：http://poj.org/problem?id=3630
 *           http://acm.hdu.edu.cn/showproblem.php?pid=1671
 * 相关题目：
 * - SPOJ PHONELST. Phone List
 * - Codeforces 633C. Spy Syndrome 2
 * - LeetCode 208. 实现 Trie (前缀树)
 * - SPOJ ADAINDEX - Ada and Indexing
 * - CodeChef DICT - Dictionary
 * 
 * 题目描述：
 * 给定n个电话号码，判断是否存在一个电话号码是另一个电话号码的前缀。
 * 如果存在输出NO，否则输出YES。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有电话号码
 * 2. 在插入过程中检查是否存在前缀关系
 * 3. 如果在插入过程中遇到已经标记为结尾的节点，说明当前字符串是之前某个字符串的前缀
 * 4. 如果在插入完成后，当前节点还有子节点，说明之前某个字符串是当前字符串的前缀
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑len(s))，其中∑len(s)是所有电话号码长度之和
 * 2. 查询过程：O(∑len(s))，遍历所有电话号码
 * 3. 总体时间复杂度：O(∑len(s))
 * 
 * 空间复杂度分析：
 * 1. Trie树空间：O(∑len(s) * 10)，每个节点最多有10个子节点(0-9)
 * 2. 总体空间复杂度：O(∑len(s))
 * 
 * 是否为最优解：是，使用Trie树可以在线性时间内检测前缀关系
 * 
 * 工程化考量：
 * 1. 异常处理：输入为空或电话号码为空的情况
 * 2. 边界情况：所有电话号码都相同的情况
 * 3. 极端输入：大量电话号码或电话号码很长的情况
 * 4. 鲁棒性：处理非法字符的情况
 * 
 * 语言特性差异：
 * Java：使用引用类型，有垃圾回收机制，HashMap实现动态子节点
 * C++：需要手动管理内存，可以使用数组或指针数组实现
 * Python：动态类型语言，字典实现自然，但性能不如编译型语言
 * 
 * 与实际应用的联系：
 * 1. 电话系统：检测电话号码前缀冲突
 * 2. 网络路由：IP地址前缀匹配
 * 3. 数据库索引：前缀索引优化查询
 */

class TrieNode {
    boolean isEnd; // 标记是否为某个电话号码的结尾
    Map<Character, TrieNode> children; // 子节点映射
    
    public TrieNode() {
        isEnd = false;
        children = new HashMap<>();
    }
}

class Trie {
    private TrieNode root;
    
    public Trie() {
        root = new TrieNode();
    }
    
    /**
     * 插入电话号码并检查前缀关系
     * @param phoneNumber 电话号码
     * @return 如果存在前缀关系返回true，否则返回false
     */
    public boolean insert(String phoneNumber) {
        TrieNode node = root;
        for (int i = 0; i < phoneNumber.length(); i++) {
            char ch = phoneNumber.charAt(i);
            // 如果字符不在当前节点的子节点中，则创建新节点
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
            // 如果在到达当前电话号码结尾之前遇到了已标记为结尾的节点，
            // 说明当前电话号码是之前某个电话号码的前缀
            if (node.isEnd) {
                return true;
            }
        }
        // 标记当前电话号码结尾
        node.isEnd = true;
        // 如果当前节点还有子节点，说明之前某个电话号码是当前电话号码的前缀
        return !node.children.isEmpty();
    }
}

public class Code03_PhoneList {
    
    /**
     * 检查电话号码列表是否存在前缀关系
     * 
     * 算法思路：
     * 1. 使用Trie树存储所有电话号码
     * 2. 在插入过程中检查是否存在前缀关系
     * 
     * 时间复杂度：O(∑len(s))，其中∑len(s)是所有电话号码长度之和
     * 空间复杂度：O(∑len(s) * 10)
     * 
     * @param phoneNumbers 电话号码列表
     * @return 如果存在前缀关系返回false，否则返回true
     */
    public static boolean isConsistent(String[] phoneNumbers) {
        Trie trie = new Trie();
        for (String phoneNumber : phoneNumbers) {
            if (trie.insert(phoneNumber)) {
                return false;
            }
        }
        return true;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：存在前缀关系
        String[] phoneNumbers1 = {"911", "97625999", "91125426"};
        System.out.println("测试用例1结果: " + (isConsistent(phoneNumbers1) ? "YES" : "NO")); // 应该输出NO
        
        // 测试用例2：不存在前缀关系
        String[] phoneNumbers2 = {"113", "12340", "123440", "12345", "98346"};
        System.out.println("测试用例2结果: " + (isConsistent(phoneNumbers2) ? "YES" : "NO")); // 应该输出YES
        
        // 测试用例3：相同号码
        String[] phoneNumbers3 = {"123", "123"};
        System.out.println("测试用例3结果: " + (isConsistent(phoneNumbers3) ? "YES" : "NO")); // 应该输出NO
    }
}