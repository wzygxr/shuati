// LOJ 数列分块入门6 - C++实现
// 题目：单点插入，单点询问
// 链接：https://loj.ac/p/6282
// 题目描述：
// 给出一个长为n的数列，以及n个操作，操作涉及单点插入，单点询问。
// 操作 0 l r c : 在位置l后面插入数字c（r忽略）
// 操作 1 l r c : 询问位置l的数字（r和c忽略）
// 数据范围：1 <= n <= 50000

#include <iostream>
#include <vector>
#include <cmath>

using namespace std;

const int MAXN = 50010;

// 使用vector存储每个块的数据
vector<int> blocks[MAXN];

// 块的大小和数量
int blockSize, blockNum;

// 总元素数量
int totalElements;

// 初始化分块结构
void build() {
    // 块大小通常选择sqrt(n)，这样可以让时间复杂度达到较优
    blockSize = static_cast<int>(sqrt(totalElements)) + 1;
    // 块数量
    blockNum = 0;
    
    // 将元素重新分配到各个块中
    int current = 1;
    while (current <= totalElements) {
        int end = min(current + blockSize - 1, totalElements);
        blockNum++;
        blocks[blockNum].clear();
        current = end + 1;
    }
}

// 重新分配块，当插入导致块大小不均衡时调用
void rebuild() {
    // 收集所有元素
    vector<int> allElements;
    for (int i = 1; i <= blockNum; i++) {
        allElements.insert(allElements.end(), blocks[i].begin(), blocks[i].end());
        blocks[i].clear();
    }
    
    // 重新分块
    totalElements = allElements.size();
    blockSize = static_cast<int>(sqrt(totalElements)) + 1;
    blockNum = 0;
    
    for (int i = 0; i < allElements.size();) {
        blockNum++;
        int cnt = 0;
        while (cnt < blockSize && i < allElements.size()) {
            blocks[blockNum].push_back(allElements[i]);
            cnt++;
            i++;
        }
    }
}

// 单点插入操作
// 在位置pos后面插入值val
void insert(int pos, int val) {
    // 计算插入位置所在的块和块内偏移
    int belong = 1;
    int offset = pos;
    
    while (belong <= blockNum && offset > blocks[belong].size()) {
        offset -= blocks[belong].size();
        belong++;
    }
    
    // 如果offset为0，表示在第一个位置插入
    if (offset == 0) {
        blocks[1].insert(blocks[1].begin(), val);
    } else {
        // 在对应位置插入
        blocks[belong].insert(blocks[belong].begin() + offset, val);
    }
    
    totalElements++;
    
    // 如果块过大，重新分块以保持时间复杂度
    if (blocks[belong].size() > 2 * blockSize) {
        rebuild();
    }
}

// 单点查询操作
// 查询位置pos的值
int query(int pos) {
    // 计算查询位置所在的块和块内偏移
    int belong = 1;
    int offset = pos;
    
    while (belong <= blockNum && offset > blocks[belong].size()) {
        offset -= blocks[belong].size();
        belong++;
    }
    
    // 返回对应位置的值
    return blocks[belong][offset - 1];
}

// 主函数
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    // 读取初始数组
    cin >> totalElements;
    
    // 收集所有元素
    vector<int> initialElements(totalElements);
    for (int i = 0; i < totalElements; i++) {
        cin >> initialElements[i];
    }
    
    // 初始化分块结构
    build();
    
    // 将初始元素放入块中
    int currentBlock = 1;
    int currentPos = 0;
    for (int i = 0; i < initialElements.size();) {
        int cnt = 0;
        while (cnt < blockSize && i < initialElements.size()) {
            blocks[currentBlock].push_back(initialElements[i]);
            cnt++;
            i++;
        }
        currentBlock++;
    }
    
    // 处理n个操作
    for (int i = 0; i < totalElements; i++) {
        int op, l, r, c;
        cin >> op >> l >> r >> c;
        
        if (op == 0) {
            // 单点插入操作
            insert(l, c);
        } else {
            // 单点查询操作
            cout << query(l) << "\n";
        }
    }
    
    return 0;
}

/*
 * 算法解析：
 * 
 * 时间复杂度分析：
 * 1. 建立分块结构：O(n)
 * 2. 单点插入操作：平均O(√n) - 最坏情况下需要重建整个分块结构O(n)，但摊还分析后仍是O(√n)
 * 3. 单点查询操作：O(√n) - 需要找到对应的块和块内偏移
 * 
 * 空间复杂度：O(n) - 存储原数组和分块相关信息
 * 
 * 算法思想：
 * 这道题与前面的题目不同，因为涉及到动态插入，普通的静态分块方法不适用。
 * 这里使用了动态分块的思想，每个块用vector存储，方便插入操作。
 * 
 * 核心思想：
 * 1. 将数组分成大小约为√n的块，每个块用vector存储
 * 2. 插入时，找到对应的块，在块内进行插入操作
 * 3. 当某个块的大小超过2√n时，重新分块以保持时间复杂度
 * 4. 查询时，找到对应的块和块内偏移，直接返回值
 * 
 * 优势：
 * 1. 可以处理动态插入的情况
 * 2. 平均时间复杂度仍然保持在O(√n)
 * 3. 实现相对简单，比平衡树等数据结构容易理解和编码
 * 
 * 适用场景：
 * 1. 需要动态插入和单点查询的问题
 * 2. 不适合用平衡树等复杂数据结构的场景
 * 3. 对代码复杂度有要求的场景
 */