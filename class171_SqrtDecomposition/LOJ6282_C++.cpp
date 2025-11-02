#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <sstream>
using namespace std;

/**
 * LOJ 6282. 数列分块入门 6 - C++实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及单点插入，单点询问。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成多个块，每个块维护一个动态数组。
 * 单点插入操作时：
 * 1. 找到要插入元素的块
 * 2. 在该块中插入元素
 * 3. 检查块大小，如果超过设定阈值，则重新分块
 * 单点查询时：
 * 1. 找到元素所在块
 * 2. 在块中查找元素
 * 
 * 时间复杂度：
 * - 单点插入：平均 O(√n)
 * - 单点查询：O(√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：通过动态维护块结构减少操作时间
 * 4. 鲁棒性：处理边界情况和特殊输入
 */

const int BLOCK_SIZE = 300;

class LOJ6282 {
private:
    vector<vector<int>> blocks;  // 存储每个块的数据
    int size;                    // 数组的当前大小
    
public:
    /**
     * 构造函数
     */
    LOJ6282() : size(0) {
        blocks.emplace_back();  // 初始化第一个块
    }
    
    /**
     * 单点插入操作
     * 
     * @param pos 插入位置（从1开始）
     * @param val 要插入的值
     */
    void insert(int pos, int val) {
        pos--; // 转换为0基索引
        
        // 找到要插入的块
        int blockIndex = 0;
        int currentPos = 0;
        while (blockIndex < blocks.size() && currentPos + blocks[blockIndex].size() <= pos) {
            currentPos += blocks[blockIndex].size();
            blockIndex++;
        }
        
        // 在对应块中插入元素
        vector<int>& targetBlock = blocks[blockIndex];
        targetBlock.insert(targetBlock.begin() + (pos - currentPos), val);
        size++;
        
        // 检查是否需要重新分块（如果块太大）
        if (targetBlock.size() > 2 * BLOCK_SIZE) {
            // 创建新块
            vector<int> newBlock;
            int mid = targetBlock.size() / 2;
            
            // 将后半部分移到新块
            for (int i = mid; i < targetBlock.size(); i++) {
                newBlock.push_back(targetBlock[i]);
            }
            // 移除原块的后半部分
            targetBlock.resize(mid);
            
            // 插入新块
            blocks.insert(blocks.begin() + blockIndex + 1, move(newBlock));
        }
    }
    
    /**
     * 单点查询操作
     * 
     * @param pos 查询位置（从1开始）
     * @return 查询结果
     */
    int query(int pos) {
        pos--; // 转换为0基索引
        
        // 找到要查询的块
        int blockIndex = 0;
        int currentPos = 0;
        while (blockIndex < blocks.size() && currentPos + blocks[blockIndex].size() <= pos) {
            currentPos += blocks[blockIndex].size();
            blockIndex++;
        }
        
        // 在对应块中查询元素
        const vector<int>& targetBlock = blocks[blockIndex];
        return targetBlock[pos - currentPos];
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    LOJ6282 solution;
    
    // 读取初始数组
    for (int i = 0; i < n; i++) {
        int value;
        cin >> value;
        solution.insert(i + 1, value); // 插入到当前数组末尾
    }
    
    // 处理操作
    for (int i = 0; i < n; i++) {
        int op, l, r, c;
        cin >> op >> l >> r >> c;
        
        if (op == 0) {
            // 单点插入
            solution.insert(l, r);
        } else {
            // 单点查询
            cout << solution.query(r) << '\n';
        }
    }
    
    return 0;
}