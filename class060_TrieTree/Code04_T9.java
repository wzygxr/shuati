package class044;

import java.util.*;
import java.io.*;

/*
 * 题目5: POJ 1451 T9
 * 题目来源：POJ
 * 题目链接：http://poj.org/problem?id=1451
 * 相关题目：
 * - LeetCode 1032. 字符流
 * - HDU 5790. Prefix
 * - Codeforces 633C. Spy Syndrome 2
 * - SPOJ ADAINDEX - Ada and Indexing
 * - LeetCode 648. 单词替换
 * 
 * 题目描述：
 * 模拟手机T9输入法。手机键盘上每个数字键对应多个字母：
 * 2: abc, 3: def, 4: ghi, 5: jkl, 6: mno, 7: pqrs, 8: tuv, 9: wxyz
 * 给定一些单词及其频率，然后给出按键序列，要求按频率从高到低输出匹配的单词。
 * 
 * 解题思路：
 * 1. 构建Trie树存储所有单词及其频率
 * 2. 对于每个节点，维护以该节点为前缀的所有单词中频率最高的单词
 * 3. 对于给定的按键序列，找到对应的Trie树节点，输出该节点存储的最高频率单词
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑len(s))，其中∑len(s)是所有单词长度之和
 * 2. 查询过程：O(m)，其中m是按键序列长度
 * 3. 总体时间复杂度：O(∑len(s) + ∑m)
 * 
 * 空间复杂度分析：
 * 1. Trie树空间：O(∑len(s) * 26)
 * 2. 总体空间复杂度：O(∑len(s))
 * 
 * 是否为最优解：是，使用Trie树可以高效地存储和查询单词
 * 
 * 工程化考量：
 * 1. 异常处理：输入为空或单词为空的情况
 * 2. 边界情况：相同单词不同频率的情况
 * 3. 极端输入：大量单词或长单词的情况
 * 4. 鲁棒性：处理非法字符的情况
 * 
 * 语言特性差异：
 * Java：使用引用类型，有垃圾回收机制，HashMap实现动态子节点
 * C++：需要手动管理内存，可以使用数组或指针数组实现
 * Python：动态类型语言，字典实现自然，但性能不如编译型语言
 * 
 * 与实际应用的联系：
 * 1. 输入法：T9输入法预测
 * 2. 搜索引擎：关键词预测
 * 3. 自动补全：代码编辑器中的自动补全功能
 */

class T9TrieNode {
    int maxFreq; // 以该节点为前缀的所有单词中的最大频率
    String maxWord; // 对应最大频率的单词
    Map<Character, T9TrieNode> children; // 子节点映射
    
    public T9TrieNode() {
        maxFreq = 0;
        maxWord = "";
        children = new HashMap<>();
    }
}

class T9Trie {
    private T9TrieNode root;
    // 数字到字母的映射
    private static final Map<Character, String> DIGIT_TO_LETTERS = new HashMap<>();
    
    static {
        DIGIT_TO_LETTERS.put('2', "abc");
        DIGIT_TO_LETTERS.put('3', "def");
        DIGIT_TO_LETTERS.put('4', "ghi");
        DIGIT_TO_LETTERS.put('5', "jkl");
        DIGIT_TO_LETTERS.put('6', "mno");
        DIGIT_TO_LETTERS.put('7', "pqrs");
        DIGIT_TO_LETTERS.put('8', "tuv");
        DIGIT_TO_LETTERS.put('9', "wxyz");
    }
    
    public T9Trie() {
        root = new T9TrieNode();
    }
    
    /**
     * 插入单词及其频率
     * @param word 单词
     * @param freq 频率
     */
    public void insert(String word, int freq) {
        T9TrieNode node = root;
        // 更新根节点的信息
        if (freq > node.maxFreq) {
            node.maxFreq = freq;
            node.maxWord = word;
        }
        
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            // 如果字符不在当前节点的子节点中，则创建新节点
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new T9TrieNode());
            }
            node = node.children.get(ch);
            // 更新当前节点的信息
            if (freq > node.maxFreq) {
                node.maxFreq = freq;
                node.maxWord = word;
            }
        }
    }
    
    /**
     * 根据按键序列查找最可能的单词
     * @param digits 按键序列
     * @return 最可能的单词
     */
    public String findMostLikelyWord(String digits) {
        T9TrieNode node = root;
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < digits.length(); i++) {
            char digit = digits.charAt(i);
            if (!DIGIT_TO_LETTERS.containsKey(digit)) {
                // 非法数字，直接返回空字符串
                return "";
            }
            
            String letters = DIGIT_TO_LETTERS.get(digit);
            // 找到第一个匹配的子节点
            T9TrieNode nextNode = null;
            for (char letter : letters.toCharArray()) {
                if (node.children.containsKey(letter)) {
                    nextNode = node.children.get(letter);
                    result.append(letter);
                    break;
                }
            }
            
            if (nextNode == null) {
                // 没有匹配的子节点，返回当前找到的部分
                break;
            }
            node = nextNode;
        }
        
        return result.toString();
    }
    
    /**
     * 获取指定前缀下的最高频率单词
     * @param prefix 前缀
     * @return 最高频率单词
     */
    public String getMostFrequentWord(String prefix) {
        T9TrieNode node = root;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            if (!node.children.containsKey(ch)) {
                return "";
            }
            node = node.children.get(ch);
        }
        return node.maxWord;
    }
}

public class Code04_T9 {
    
    /**
     * T9输入法模拟
     * 
     * 算法思路：
     * 1. 构建Trie树存储所有单词及其频率
     * 2. 对于每个节点，维护以该节点为前缀的所有单词中频率最高的单词
     * 3. 对于给定的按键序列，找到对应的Trie树节点，输出该节点存储的最高频率单词
     * 
     * 时间复杂度：O(∑len(s) + ∑m)
     * 空间复杂度：O(∑len(s))
     * 
     * @param words 单词数组
     * @param frequencies 频率数组
     * @param digits 按键序列
     * @return 最可能的单词
     */
    public static String t9Input(String[] words, int[] frequencies, String digits) {
        T9Trie trie = new T9Trie();
        
        // 构建Trie树
        for (int i = 0; i < words.length; i++) {
            trie.insert(words[i], frequencies[i]);
        }
        
        // 查找最可能的单词
        return trie.findMostLikelyWord(digits);
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例
        String[] words = {"apple", "application", "apply", "banana", "band", "bandana"};
        int[] frequencies = {50, 30, 20, 40, 25, 15};
        String digits = "27753"; // 对应apple
        
        String result = t9Input(words, frequencies, digits);
        System.out.println("输入按键序列 " + digits + " 最可能的单词是: " + result);
        
        // 另一个测试用例
        String digits2 = "226"; // 对应banana
        String result2 = t9Input(words, frequencies, digits2);
        System.out.println("输入按键序列 " + digits2 + " 最可能的单词是: " + result2);
    }
}