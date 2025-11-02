package class045_Trie;

import java.util.*;

/**
 * SPOJ SUBXOR
 * 
 * 题目描述：
 * 给定一个数组和一个值k，统计有多少个子数组的异或值小于k。
 * 
 * 解题思路：
 * 1. 使用前缀异或和将问题转化为：对于每个位置i，统计有多少个j<i满足prefix[i] ^ prefix[j] < k
 * 2. 使用Trie树存储所有prefix[j]的二进制表示
 * 3. 对于每个prefix[i]，在Trie树中查找有多少个prefix[j]满足prefix[i] ^ prefix[j] < k
 * 4. 贪心策略：从最高位开始比较，如果当前位异或值小于k的对应位，则加上该子树的所有节点数
 * 
 * 时间复杂度：O(N*32) = O(N)，其中N是数组长度，32是整数的位数
 * 空间复杂度：O(N*32) = O(N)
 */
public class Code24_SPOJSUBXOR {
    
    // Trie树节点类
    static class TrieNode {
        TrieNode[] children = new TrieNode[2]; // 子节点数组，对应0和1
        int count = 0; // 经过该节点的数字数量
    }
    
    static TrieNode root = new TrieNode(); // Trie树根节点
    
    /**
     * 向Trie树中插入一个数字的二进制表示
     * @param num 要插入的数字
     */
    public static void insert(int num) {
        TrieNode node = root;
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1; // 获取第i位的值
            if (node.children[bit] == null) {
                node.children[bit] = new TrieNode();
            }
            node = node.children[bit];
            node.count++; // 增加经过该节点的数字数量
        }
    }
    
    /**
     * 查找有多少个数字与给定数字异或值小于k
     * @param num 给定的数字
     * @param k 比较值
     * @return 满足条件的数字数量
     */
    public static int countLessThanK(int num, int k) {
        TrieNode node = root;
        int result = 0;
        
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            if (node == null) {
                break;
            }
            
            int numBit = (num >> i) & 1; // num的第i位
            int kBit = (k >> i) & 1; // k的第i位
            
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
    
    /**
     * 统计有多少个子数组的异或值小于k
     * @param nums 输入数组
     * @param k 比较值
     * @return 满足条件的子数组数量
     */
    public static long countSubarraysWithXorLessThanK(int[] nums, int k) {
        // 重新初始化Trie树
        root = new TrieNode();
        
        long result = 0;
        int prefixXor = 0;
        
        // 插入前缀异或和0（表示空前缀）
        insert(0);
        
        // 遍历数组
        for (int i = 0; i < nums.length; i++) {
            prefixXor ^= nums[i]; // 计算前缀异或和
            
            // 查找有多少个之前的前缀异或和与当前前缀异或和的异或值小于k
            result += countLessThanK(prefixXor, k);
            
            // 将当前前缀异或和插入Trie树
            insert(prefixXor);
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int t = scanner.nextInt(); // 测试用例数量
        
        for (int i = 0; i < t; i++) {
            int n = scanner.nextInt(); // 数组长度
            int k = scanner.nextInt(); // 比较值
            
            int[] nums = new int[n];
            // 读取数组元素
            for (int j = 0; j < n; j++) {
                nums[j] = scanner.nextInt();
            }
            
            // 计算并输出结果
            long result = countSubarraysWithXorLessThanK(nums, k);
            System.out.println(result);
        }
        
        scanner.close();
    }
}