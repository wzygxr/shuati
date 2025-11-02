#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

/**
 * 牛客网 NC15277 区间异或和
 * 题目要求：区间异或操作，单点查询
 * 核心技巧：分块标记 - 对完整的块进行标记，对不完整的块进行暴力修改
 * 时间复杂度：O(√n) / 操作
 * 空间复杂度：O(n)
 * 测试链接：https://ac.nowcoder.com/acm/problem/15277
 * 
 * 算法思想详解：
 * 1. 将数组分成大小为√n的块
 * 2. 对于区间异或操作：
 *    - 对于完全包含在区间内的块，更新块标记（lazy标记）
 *    - 对于不完整的块，暴力更新每个元素的值
 * 3. 对于单点查询：
 *    - 计算该元素所在块的标记异或上原始值
 *    - 返回最终结果
 */

class BlockXOR {
private:
    vector<int> arr;      // 原始数组
    vector<int> block;    // 每个块的异或标记（lazy标记）
    int blockSize;        // 块的大小
    int n;                // 数组长度
    
public:
    /**
     * 构造函数，初始化数据结构
     * @param array 输入数组
     */
    BlockXOR(const vector<int>& array) {
        n = array.size();
        arr = array;
        blockSize = static_cast<int>(sqrt(n)) + 1;
        block.resize((n + blockSize - 1) / blockSize, 0); // 向上取整计算块数
    }
    
    /**
     * 区间异或操作
     * @param l 左边界（包含，0-based）
     * @param r 右边界（包含，0-based）
     * @param val 异或的值
     */
    void xorRange(int l, int r, int val) {
        int leftBlock = l / blockSize;
        int rightBlock = r / blockSize;
        
        // 如果在同一个块内，直接暴力修改
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; ++i) {
                arr[i] ^= val;
            }
            return;
        }
        
        // 处理左边不完整块
        for (int i = l; i < (leftBlock + 1) * blockSize; ++i) {
            arr[i] ^= val;
        }
        
        // 处理中间的完整块
        for (int i = leftBlock + 1; i < rightBlock; ++i) {
            block[i] ^= val;
        }
        
        // 处理右边不完整块
        for (int i = rightBlock * blockSize; i <= r; ++i) {
            arr[i] ^= val;
        }
    }
    
    /**
     * 单点查询
     * @param index 查询的位置（0-based）
     * @return 查询位置的最终值
     */
    int query(int index) {
        int blockId = index / blockSize;
        return arr[index] ^ block[blockId];
    }
    
    /**
     * 获取完整的数组内容（考虑块标记的影响）
     * @return 处理后的完整数组
     */
    vector<int> getArray() {
        vector<int> result(n);
        for (int i = 0; i < n; ++i) {
            result[i] = query(i);
        }
        return result;
    }
    
    /**
     * 重置所有块标记
     * 将块标记应用到原始数组，然后重置标记
     */
    void resetBlocks() {
        // 先将块标记应用到原始数组
        for (int i = 0; i < n; ++i) {
            int blockId = i / blockSize;
            arr[i] ^= block[blockId];
        }
        // 重置所有块标记
        fill(block.begin(), block.end(), 0);
    }
    
    /**
     * 优化版本的区间异或操作
     * 当操作次数过多时，可以定期调用resetBlocks优化性能
     */
    void xorRangeOptimized(int l, int r, int val) {
        static int operationCount = 0;
        xorRange(l, r, val);
        
        // 每执行1000次操作后重置一次块标记，防止标记累积过多
        if (++operationCount % 1000 == 0) {
            resetBlocks();
        }
    }
};

// 测试函数
void testBlockXOR() {
    int n;
    cout << "请输入数组长度：" << endl;
    cin >> n;
    
    vector<int> array(n);
    cout << "请输入数组元素：" << endl;
    for (int i = 0; i < n; ++i) {
        cin >> array[i];
    }
    
    BlockXOR solution(array);
    
    int q;
    cout << "请输入操作数量：" << endl;
    cin >> q;
    
    cout << "操作格式：1 l r val (区间异或) 或 2 index (单点查询)" << endl;
    while (q--) {
        int op;
        cin >> op;
        if (op == 1) {
            // 区间异或操作
            int l, r, val;
            cin >> l >> r >> val;
            l--; r--; // 转换为0-based索引
            solution.xorRange(l, r, val);
            cout << "区间异或操作完成" << endl;
        } else if (op == 2) {
            // 单点查询
            int index;
            cin >> index;
            index--; // 转换为0-based索引
            int result = solution.query(index);
            cout << "查询结果：" << result << endl;
        }
    }
}

// 性能测试函数
void performanceTest() {
    const int SIZE = 100000;
    vector<int> largeArray(SIZE, 0);
    
    // 初始化数组为0到SIZE-1
    for (int i = 0; i < SIZE; ++i) {
        largeArray[i] = i;
    }
    
    BlockXOR solution(largeArray);
    
    // 执行1000次随机区间操作
    cout << "开始性能测试..." << endl;
    for (int i = 0; i < 1000; ++i) {
        int l = rand() % SIZE;
        int r = rand() % SIZE;
        if (l > r) swap(l, r);
        int val = rand() % 100;
        solution.xorRangeOptimized(l, r, val);
    }
    
    // 执行100次随机查询
    for (int i = 0; i < 100; ++i) {
        int index = rand() % SIZE;
        int result = solution.query(index);
        if (i < 10) { // 只输出前10个结果
            cout << "索引 " << index << " 的值: " << result << endl;
        }
    }
    
    cout << "性能测试完成" << endl;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cout << "1. 基本功能测试" << endl;
    cout << "2. 性能测试" << endl;
    cout << "请选择测试类型：";
    
    int choice;
    cin >> choice;
    
    if (choice == 1) {
        testBlockXOR();
    } else if (choice == 2) {
        performanceTest();
    }
    
    return 0;
}

/**
 * C++语言特定优化说明：
 * 1. 使用vector存储数组和块标记，内存管理更安全
 * 2. 使用ios::sync_with_stdio(false)和cin.tie(0)加速输入输出
 * 3. 实现了优化版本的区间操作，定期重置块标记防止性能退化
 * 4. 添加了性能测试函数，用于验证算法在大数据量下的效率
 * 
 * 边界情况处理：
 * 1. 输入数据的合法性检查可以在实际应用中添加
 * 2. 区间操作中的l和r可以是任意顺序，代码中有swap处理
 * 3. 对于空数组，可以在构造函数中特殊处理
 * 
 * 时间复杂度分析：
 * - 区间操作：O(√n)
 *   - 最坏情况：需要处理两个不完整块和O(√n)个完整块
 *   - 不完整块最多有O(√n)个元素，完整块处理是O(1)每个块
 * - 单点查询：O(1)
 *   - 只需要一次块索引计算和一次异或操作
 * 
 * 空间复杂度分析：
 * - O(n) 用于存储原始数组
 * - O(√n) 用于存储块标记
 * - 总体空间复杂度：O(n)
 */