package class032;

import java.util.*;

// LeetCode 2166. Design Bitset
// 题目链接: https://leetcode.com/problems/design-bitset/
// 题目大意:
// 实现一个Bitset类，支持以下操作:
// 1. Bitset(int size): 用size个位初始化Bitset，所有位都是0
// 2. void fix(int idx): 将下标为idx的位更新为1
// 3. void unfix(int idx): 将下标为idx的位更新为0
// 4. void flip(): 翻转所有位的值
// 5. boolean all(): 检查所有位是否都是1
// 6. boolean one(): 检查是否至少有一位是1
// 7. int count(): 返回所有位中1的数量
// 8. String toString(): 返回所有位的状态

// 解题思路:
// 1. 使用int数组来存储位信息，每个int可以存储32位
// 2. 使用懒标记优化flip操作，避免每次都实际翻转所有位
// 3. 维护实际的1的个数，避免每次count都重新计算
// 时间复杂度分析:
// - fix, unfix: O(1)
// - flip: O(1)
// - all, one, count: O(1)
// - toString: O(size)
// 空间复杂度: O(size/32)

public class Code05_DesignBitset {
    
    // Design Bitset实现类
    // 实现一个高效的位集数据结构，支持多种位操作
    static class Bitset {
        // 存储位信息的数组，每个int可以存储32位
        private int[] bits;
        // 位的总数
        private int size;
        // 当前1的个数，用于优化count操作
        private int ones;
        // 是否翻转的标记，用于优化flip操作
        // true表示逻辑状态与实际存储状态相反
        private boolean flipped;
        
        // 构造函数，初始化size个位，所有位都是0
        // 参数size表示位集的大小
        public Bitset(int size) {
            // 计算需要多少个int来存储size位
            // (size + 31) / 32 是向上取整的写法
            // 例如：size=100，则需要(100+31)/32 = 4个int来存储100位
            this.bits = new int[(size + 31) / 32];
            this.size = size;
            // 初始状态下所有位都是0，所以1的个数为0
            this.ones = 0;
            // 初始状态下没有翻转
            this.flipped = false;
        }
        
        // 将下标为idx的位更新为1
        // 参数idx表示要设置为1的位的下标
        public void fix(int idx) {
            // 计算idx在数组中的位置和位偏移
            // arrayIdx确定在bits数组中的哪个int
            int arrayIdx = idx / 32;
            // bitIdx确定在该int中的哪一位
            int bitIdx = idx % 32;
            
            // 如果当前状态(考虑翻转)下该位是0，则设置为1
            if (flipped) {
                // 如果翻转了，实际的1在bits中是0
                // 检查该位是否为1（在逻辑上是0）
                if ((bits[arrayIdx] & (1 << bitIdx)) != 0) {
                    // 该位实际是1，但在逻辑上是0，需要设置为1（即实际设置为0）
                    // 使用异或操作将该位设置为0
                    bits[arrayIdx] ^= (1 << bitIdx);
                    // 1的个数增加1
                    ones++;
                }
            } else {
                // 如果没有翻转，实际的1在bits中是1
                // 检查该位是否为0
                if ((bits[arrayIdx] & (1 << bitIdx)) == 0) {
                    // 该位实际是0，需要设置为1
                    // 使用按位或操作将该位设置为1
                    bits[arrayIdx] |= (1 << bitIdx);
                    // 1的个数增加1
                    ones++;
                }
            }
        }
        
        // 将下标为idx的位更新为0
        // 参数idx表示要设置为0的位的下标
        public void unfix(int idx) {
            // 计算idx在数组中的位置和位偏移
            // arrayIdx确定在bits数组中的哪个int
            int arrayIdx = idx / 32;
            // bitIdx确定在该int中的哪一位
            int bitIdx = idx % 32;
            
            // 如果当前状态(考虑翻转)下该位是1，则设置为0
            if (flipped) {
                // 如果翻转了，实际的0在bits中是1
                // 检查该位是否为0（在逻辑上是1）
                if ((bits[arrayIdx] & (1 << bitIdx)) == 0) {
                    // 该位实际是0，但在逻辑上是1，需要设置为0（即实际设置为1）
                    // 使用按位或操作将该位设置为1
                    bits[arrayIdx] |= (1 << bitIdx);
                    // 1的个数减少1
                    ones--;
                }
            } else {
                // 如果没有翻转，实际的0在bits中是0
                // 检查该位是否为1
                if ((bits[arrayIdx] & (1 << bitIdx)) != 0) {
                    // 该位实际是1，需要设置为0
                    // 使用异或操作将该位设置为0
                    bits[arrayIdx] ^= (1 << bitIdx);
                    // 1的个数减少1
                    ones--;
                }
            }
        }
        
        // 翻转所有位的值
        // 使用懒标记优化，避免每次都实际翻转所有位
        public void flip() {
            // 切换翻转标记
            flipped = !flipped;
            // 翻转后，1的个数变为总位数减去原来的1的个数
            // 这是基于数学原理：0变1，1变0，所以1的个数变为size-ones
            ones = size - ones;
        }
        
        // 检查所有位是否都是1
        // 返回值：如果所有位都是1返回true，否则返回false
        public boolean all() {
            // 所有位都是1当且仅当1的个数等于总位数
            return ones == size;
        }
        
        // 检查是否至少有一位是1
        // 返回值：如果至少有一位是1返回true，否则返回false
        public boolean one() {
            // 至少有一位是1当且仅当1的个数大于0
            return ones > 0;
        }
        
        // 返回所有位中1的数量
        // 返回值：1的数量
        public int count() {
            // 直接返回维护的1的个数，避免重新计算
            return ones;
        }
        
        // 返回所有位的状态
        // 返回值：表示所有位状态的字符串
        public String toString() {
            // 使用StringBuilder提高字符串拼接效率
            StringBuilder sb = new StringBuilder();
            // 遍历每一位
            for (int i = 0; i < size; i++) {
                // 计算第i位在数组中的位置和位偏移
                int arrayIdx = i / 32;
                int bitIdx = i % 32;
                
                // 根据是否翻转来确定实际的位值
                int bitValue;
                if (flipped) {
                    // 如果翻转了，实际的1在bits中是0
                    // 检查bits中该位是否为1
                    bitValue = ((bits[arrayIdx] & (1 << bitIdx)) != 0) ? 0 : 1;
                } else {
                    // 如果没有翻转，实际的1在bits中是1
                    // 检查bits中该位是否为1
                    bitValue = ((bits[arrayIdx] & (1 << bitIdx)) != 0) ? 1 : 0;
                }
                // 将位值添加到结果字符串中
                sb.append(bitValue);
            }
            return sb.toString();
        }
    }
    
    // 测试用例
    // 验证Bitset类的正确性
    public static void main(String[] args) {
        System.out.println("LeetCode 2166. Design Bitset 解题测试");
        
        // 创建一个5位的Bitset
        Bitset bs = new Bitset(5);
        
        // 初始状态: "00000"
        System.out.println("Initial: " + bs.toString());  // 应该输出 "00000"
        
        // fix(3) -> "00010"
        bs.fix(3);
        System.out.println("After fix(3): " + bs.toString());  // 应该输出 "00010"
        
        // fix(1) -> "01010"
        bs.fix(1);
        System.out.println("After fix(1): " + bs.toString());  // 应该输出 "01010"
        
        // flip() -> "10101"
        bs.flip();
        System.out.println("After flip(): " + bs.toString());  // 应该输出 "10101"
        
        // all() -> false
        System.out.println("all(): " + bs.all());  // 应该输出 false
        
        // unfix(0) -> "00101"
        bs.unfix(0);
        System.out.println("After unfix(0): " + bs.toString());  // 应该输出 "00101"
        
        // flip() -> "11010"
        bs.flip();
        System.out.println("After flip(): " + bs.toString());  // 应该输出 "11010"
        
        // one() -> true
        System.out.println("one(): " + bs.one());  // 应该输出 true
        
        // fix(3) -> "11010" (已经是1了，无变化)
        bs.fix(3);
        System.out.println("After fix(3): " + bs.toString());  // 应该输出 "11010"
        
        // count() -> 4
        System.out.println("count(): " + bs.count());  // 应该输出 4
        
        // toString() -> "11010"
        System.out.println("toString(): " + bs.toString());  // 应该输出 "11010"
    }
}