#include <iostream>
#include <vector>
#include <map>
#include <algorithm>

using namespace std;

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

// Trie树节点结构
struct TrieNode {
    map<int, TrieNode*> children; // 子节点映射，对应0和1
    int count;                    // 经过该节点的数字数量
    vector<int> indices;          // 存储经过该节点的数字索引
    
    TrieNode() {
        count = 0;
    }
};

TrieNode* root;
vector<int> numbers; // 存储所有数字
int index = 0;       // 当前数字索引

/**
 * 初始化数据结构
 */
void init() {
    root = new TrieNode();
    numbers.clear();
    index = 0;
}

/**
 * 向Trie树中插入一个数字
 * @param num 要插入的数字
 * @param idx 数字的索引
 */
void insert(int num, int idx) {
    TrieNode* node = root;
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1; // 获取第i位的值
        if (node->children.find(bit) == node->children.end()) {
            node->children[bit] = new TrieNode();
        }
        node = node->children[bit];
        node->count++; // 增加经过该节点的数字数量
        node->indices.push_back(idx); // 记录数字索引
    }
}

/**
 * 从Trie树中删除一个数字
 * @param num 要删除的数字
 * @param idx 数字的索引
 */
void deleteNum(int num, int idx) {
    TrieNode* node = root;
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1; // 获取第i位的值
        if (node->children.find(bit) != node->children.end()) {
            node = node->children[bit];
            node->count--; // 减少经过该节点的数字数量
            // 移除数字索引
            auto it = find(node->indices.begin(), node->indices.end(), idx);
            if (it != node->indices.end()) {
                node->indices.erase(it);
            }
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
int maxXor(int num) {
    TrieNode* node = root;
    int result = 0;
    
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1; // 获取第i位的值
        int oppositeBit = 1 - bit; // 相反位
        
        // 贪心策略：尽可能选择与当前位相反的位
        if (node->children.find(oppositeBit) != node->children.end() && 
            node->children[oppositeBit]->count > 0) {
            result |= (1 << i); // 设置结果的第i位为1
            node = node->children[oppositeBit];
        } else {
            node = node->children[bit];
        }
    }
    
    return result;
}

/**
 * 查询与给定数字异或值最小的数字
 * @param num 给定的数字
 * @return 最小异或值
 */
int minXor(int num) {
    TrieNode* node = root;
    int result = 0;
    
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1; // 获取第i位的值
        
        // 贪心策略：尽可能选择与当前位相同的位
        if (node->children.find(bit) != node->children.end() && 
            node->children[bit]->count > 0) {
            node = node->children[bit];
        } else {
            result |= (1 << i); // 设置结果的第i位为1
            node = node->children[1 - bit];
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
int countXorLessThanK(int num, int k) {
    TrieNode* node = root;
    int result = 0;
    
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        if (node == nullptr) {
            break;
        }
        
        int numBit = (num >> i) & 1; // num的第i位
        int kBit = (k >> i) & 1;     // k的第i位
        
        if (kBit == 1) {
            // 如果k的第i位是1，那么异或值为0的子树都满足条件
            if (node->children.find(numBit) != node->children.end()) {
                result += node->children[numBit]->count;
            }
            // 继续处理异或值为1的子树
            node = node->children[1 - numBit];
        } else {
            // 如果k的第i位是0，只能继续处理异或值为0的子树
            node = node->children[numBit];
        }
    }
    
    return result;
}

int main() {
    init(); // 初始化数据结构
    
    int q;
    cin >> q; // 查询数量
    
    for (int i = 0; i < q; i++) {
        int type;
        cin >> type;
        
        switch (type) {
            case 0: { // 添加数字
                int x;
                cin >> x;
                numbers.push_back(x);
                insert(x, index);
                index++;
                break;
            }
            
            case 1: { // 查询最大异或值
                int y;
                cin >> y;
                int maxResult = maxXor(y);
                cout << maxResult << endl;
                break;
            }
            
            case 2: { // 查询最小异或值
                int z;
                cin >> z;
                int minResult = minXor(z);
                cout << minResult << endl;
                break;
            }
            
            case 3: { // 查询异或值小于等于k的数字数量
                int a, k;
                cin >> a >> k;
                int countResult = countXorLessThanK(a, k);
                cout << countResult << endl;
                break;
            }
            
            case 4: { // 删除指定位置插入的数字
                int p;
                cin >> p;
                int numToDelete = numbers[p];
                deleteNum(numToDelete, p);
                break;
            }
        }
    }
    
    return 0;
}