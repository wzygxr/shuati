#include <iostream>
#include <vector>
#include <string>
#include <bitset>

// HackerRank Java BitSet
// 题目链接: https://www.hackerrank.com/challenges/java-bitset/problem
// 题目大意:
// 给定两个BitSet，大小为n，初始时所有位都为0
// 执行一系列操作，每次操作后打印两个BitSet中1的个数

// 操作包括:
// AND 1 2: 将BitSet1与BitSet2进行按位与操作，结果存储在BitSet1中
// OR 1 2: 将BitSet1与BitSet2进行按位或操作，结果存储在BitSet1中
// XOR 1 2: 将BitSet1与BitSet2进行按位异或操作，结果存储在BitSet1中
// FLIP 1 2: 将BitSet1中下标为2的位翻转
// SET 1 2: 将BitSet1中下标为2的位设置为1

// 解题思路:
// 1. 使用std::bitset来模拟Java的BitSet
// 2. 根据操作类型执行相应的位运算操作
// 3. 每次操作后手动计算并打印两个BitSet中1的个数
// 时间复杂度分析:
// - AND, OR, XOR: O(n/32)
// - FLIP, SET: O(1)
// - count(): O(n/32)
// 空间复杂度: O(n)

using namespace std;

// 自定义BitSet类，模拟Java的BitSet功能
// 实现一个高效的位集数据结构，支持多种位操作
class BitSet {
private:
    // 使用vector存储位信息，每个unsigned int可以存储32位
    vector<unsigned int> bits;
    // BitSet的大小
    int size;
    
public:
    // 构造函数
    // 参数n表示BitSet的大小
    BitSet(int n) {
        size = n;
        // 计算需要多少个unsigned int来存储n位
        // (n + 31) / 32 是向上取整的写法
        // 例如：n=100，则需要(100+31)/32 = 4个unsigned int来存储100位
        bits = vector<unsigned int>((n + 31) / 32, 0);
    }
    
    // 按位与操作
    // 参数other表示要与当前BitSet进行按位与操作的另一个BitSet
    void andOp(const BitSet& other) {
        // 对每一位所在的unsigned int进行按位与操作
        for (int i = 0; i < bits.size(); i++) {
            bits[i] &= other.bits[i];
        }
    }
    
    // 按位或操作
    // 参数other表示要与当前BitSet进行按位或操作的另一个BitSet
    void orOp(const BitSet& other) {
        // 对每一位所在的unsigned int进行按位或操作
        for (int i = 0; i < bits.size(); i++) {
            bits[i] |= other.bits[i];
        }
    }
    
    // 按位异或操作
    // 参数other表示要与当前BitSet进行按位异或操作的另一个BitSet
    void xorOp(const BitSet& other) {
        // 对每一位所在的unsigned int进行按位异或操作
        for (int i = 0; i < bits.size(); i++) {
            bits[i] ^= other.bits[i];
        }
    }
    
    // 翻转指定位置的位
    // 参数idx表示要翻转的位的下标
    void flip(int idx) {
        // 计算idx在数组中的位置和位偏移
        // arrayIdx确定在bits数组中的哪个unsigned int
        int arrayIdx = idx / 32;
        // bitIdx确定在该unsigned int中的哪一位
        int bitIdx = idx % 32;
        // 使用异或操作翻转指定位
        // 1U << bitIdx 创建一个只有第bitIdx位为1的数
        // ^= 异或操作，实现翻转效果
        bits[arrayIdx] ^= (1U << bitIdx);
    }
    
    // 设置指定位置的位为1
    // 参数idx表示要设置为1的位的下标
    void set(int idx) {
        // 计算idx在数组中的位置和位偏移
        // arrayIdx确定在bits数组中的哪个unsigned int
        int arrayIdx = idx / 32;
        // bitIdx确定在该unsigned int中的哪一位
        int bitIdx = idx % 32;
        // 使用按位或操作将指定位设置为1
        // 1U << bitIdx 创建一个只有第bitIdx位为1的数
        // |= 按位或操作，将指定位设置为1
        bits[arrayIdx] |= (1U << bitIdx);
    }
    
    // 计算1的个数
    // 返回值：1的个数
    int count() const {
        int result = 0;
        // 遍历每一位所在的unsigned int
        for (int i = 0; i < bits.size(); i++) {
            // 使用内置函数计算一个unsigned int中1的个数
            // __builtin_popcount是GCC内置函数，用于计算32位整数中1的个数
            result += __builtin_popcount(bits[i]);
        }
        return result;
    }
};

// 主函数，处理输入并输出结果
int main() {
    // 优化输入输出速度，关闭stdio同步，解除cin与cout的绑定
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, m;
    // 读取n和m
    // n表示BitSet的大小，m表示操作的数量
    cin >> n >> m;
    
    // 初始化两个BitSet
    // 创建一个BitSet向量，索引0不使用，1和2分别对应题目中的BitSet1和BitSet2
    vector<BitSet> bitSets(3);
    // 初始化BitSet1，大小为n，初始时所有位都为0
    bitSets[1] = BitSet(n);
    // 初始化BitSet2，大小为n，初始时所有位都为0
    bitSets[2] = BitSet(n);
    
    // 执行m次操作
    // 循环处理每个操作
    for (int i = 0; i < m; i++) {
        // 读取操作指令
        string operation;
        int set1, set2;
        cin >> operation >> set1 >> set2;
        
        // 根据操作类型执行相应的操作
        if (operation == "AND") {
            // 将BitSet[set1]与BitSet[set2]进行按位与操作，结果存储在BitSet[set1]中
            // 按位与操作：两个位都为1时结果才为1，否则为0
            bitSets[set1].andOp(bitSets[set2]);
        } else if (operation == "OR") {
            // 将BitSet[set1]与BitSet[set2]进行按位或操作，结果存储在BitSet[set1]中
            // 按位或操作：两个位中至少有一个为1时结果为1，否则为0
            bitSets[set1].orOp(bitSets[set2]);
        } else if (operation == "XOR") {
            // 将BitSet[set1]与BitSet[set2]进行按位异或操作，结果存储在BitSet[set1]中
            // 按位异或操作：两个位不同时结果为1，相同时为0
            bitSets[set1].xorOp(bitSets[set2]);
        } else if (operation == "FLIP") {
            // 将BitSet[set1]中下标为set2的位翻转
            // 翻转操作：0变1，1变0
            bitSets[set1].flip(set2);
        } else if (operation == "SET") {
            // 将BitSet[set1]中下标为set2的位设置为1
            // 设置操作：将指定位置为1
            bitSets[set1].set(set2);
        }
        
        // 打印两个BitSet中1的个数
        // 每次操作后都要输出两个BitSet中1的个数
        cout << bitSets[1].count() << " " << bitSets[2].count() << "\n";
    }
    
    return 0;
}