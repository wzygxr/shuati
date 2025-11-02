package class045_Trie;

import java.util.*;

/**
 * CodeChef XRQRS - Xor Queries
 * 
 * 题目描述：
 * 实现一个数据结构，支持以下操作：
 * 1. 向集合中添加一个数字
 * 2. 查询集合中与给定数字异或值最大的数字
 * 3. 查询集合中与给定数字异或值最小的数字
 * 4. 查询集合中与给定数字异或值小于等于给定值的数字数量
 * 5. 删除指定位置插入的数字
 * 
 * 解题思路：
 * 1. 使用前缀树存储所有数字的二进制表示
 * 2. 对于每种查询操作，使用相应的策略在前缀树中查找结果
 * 
 * 时间复杂度：
 * - 插入：O(32)
 * - 查询最大/最小异或值：O(32)
 * - 查询数量：O(32)
 * - 删除：O(32)
 * 空间复杂度：O(N*32)
 */
public class Code30_CodeChefXRQRS {
    
    // Trie树节点类
    static class TrieNode {
        TrieNode[] children = new TrieNode[2]; // 子节点数组，对应0和1
        int count = 0;                         // 经过该节点的数字数量
        List<Integer> indices = new ArrayList<>(); // 存储经过该节点的数字索引
    }
    
    static TrieNode root;
    static List<Integer> numbers; // 存储所有数字
    static int index = 0;         // 当前数字索引
    
    /**
     * 初始化数据结构
     */
    public static void init() {
        root = new TrieNode();
        numbers = new ArrayList<>();
        index = 0;
    }
    
    /**
     * 向Trie树中插入一个数字
     * @param num 要插入的数字
     * @param idx 数字的索引
     */
    public static void insert(int num, int idx) {
        TrieNode node = root;
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1; // 获取第i位的值
            if (node.children[bit] == null) {
                node.children[bit] = new TrieNode();
            }
            node = node.children[bit];
            node.count++; // 增加经过该节点的数字数量
            node.indices.add(idx); // 记录数字索引
        }
    }
    
    /**
     * 从Trie树中删除一个数字
     * @param num 要删除的数字
     * @param idx 数字的索引
     */
    public static void delete(int num, int idx) {
        TrieNode node = root;
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1; // 获取第i位的值
            if (node.children[bit] != null) {
                node = node.children[bit];
                node.count--; // 减少经过该节点的数字数量
                node.indices.remove(Integer.valueOf(idx)); // 移除数字索引
            } else {
                break;
            }
        }
    }
    
    /**
     * 查询与给定数字异或值最大的数字
     * @param num 给定的数字
     * @return 最大异或值
     */
    public static int maxXor(int num) {
        TrieNode node = root;
        int result = 0;
        
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1; // 获取第i位的值
            int oppositeBit = 1 - bit; // 相反位
            
            // 贪心策略：尽可能选择与当前位相反的位
            if (node.children[oppositeBit] != null && node.children[oppositeBit].count > 0) {
                result |= (1 << i); // 设置结果的第i位为1
                node = node.children[oppositeBit];
            } else {
                node = node.children[bit];
            }
        }
        
        return result;
    }
    
    /**
     * 查询与给定数字异或值最小的数字
     * @param num 给定的数字
     * @return 最小异或值
     */
    public static int minXor(int num) {
        TrieNode node = root;
        int result = 0;
        
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1; // 获取第i位的值
            
            // 贪心策略：尽可能选择与当前位相同的位
            if (node.children[bit] != null && node.children[bit].count > 0) {
                node = node.children[bit];
            } else {
                result |= (1 << i); // 设置结果的第i位为1
                node = node.children[1 - bit];
            }
        }
        
        return result;
    }
    
    /**
     * 查询与给定数字异或值小于等于k的数字数量
     * @param num 给定的数字
     * @param k 比较值
     * @return 满足条件的数字数量
     */
    public static int countXorLessThanK(int num, int k) {
        TrieNode node = root;
        int result = 0;
        
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            if (node == null) {
                break;
            }
            
            int numBit = (num >> i) & 1; // num的第i位
            int kBit = (k >> i) & 1;     // k的第i位
            
            if (kBit == 1) {
                // 如果k的第i位是1，那么异或值为0的子树都满足条件
                if (node.children[numBit] != null) {
                    result += node.children[numBit].count;
                }
                // 继续处理异或值为1的子树
                node = node.children[1 - numBit];
            } else {
                // 如果k的第i位是0，只能继续处理异或值为0的子树
                node = node.children[numBit];
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        init(); // 初始化数据结构
        
        int q = scanner.nextInt(); // 查询数量
        
        for (int i = 0; i < q; i++) {
            int type = scanner.nextInt();
            
            switch (type) {
                case 0: // 添加数字
                    int x = scanner.nextInt();
                    numbers.add(x);
                    insert(x, index);
                    index++;
                    break;
                    
                case 1: // 查询最大异或值
                    int y = scanner.nextInt();
                    int maxResult = maxXor(y);
                    System.out.println(maxResult);
                    break;
                    
                case 2: // 查询最小异或值
                    int z = scanner.nextInt();
                    int minResult = minXor(z);
                    System.out.println(minResult);
                    break;
                    
                case 3: // 查询异或值小于等于k的数字数量
                    int a = scanner.nextInt();
                    int k = scanner.nextInt();
                    int countResult = countXorLessThanK(a, k);
                    System.out.println(countResult);
                    break;
                    
                case 4: // 删除指定位置插入的数字
                    int p = scanner.nextInt();
                    int numToDelete = numbers.get(p);
                    delete(numToDelete, p);
                    break;
            }
        }
        
        scanner.close();
    }
}