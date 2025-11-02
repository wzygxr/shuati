// GIVEAWAY - 区间查询与更新问题 - 分块算法实现 (C++版本)
// 题目来源: SPOJ
// 题目链接: https://www.spoj.com/problems/GIVEAWAY/
// 题目大意: 维护一个数组，支持两种操作：
// 1. C x y: 将位置x的值更新为y
// 2. Q l r k: 查询区间[l,r]内大于等于k的元素个数
// 约束条件: 数组长度n ≤ 5*10^5，操作次数q ≤ 10^5

#include <algorithm>
#include <cmath>
#include <vector>
using namespace std;

const int MAXN = 500005;

int n, q, blen, blockCount;
int arr[MAXN];           // 原始数组
int block[MAXN];         // 每个元素所属的块
vector<int> blockElements[MAXN]; // 每个块中排序后的元素

// 初始化分块结构
void init() {
    blen = (int)sqrt(n);
    if (blen == 0) blen = 1;
    blockCount = (n + blen - 1) / blen;
    
    // 为每个元素分配块
    for (int i = 1; i <= n; i++) {
        block[i] = (i - 1) / blen;
    }
    
    // 将元素分配到对应的块中并排序
    for (int i = 1; i <= n; i++) {
        blockElements[block[i]].push_back(arr[i]);
    }
    
    // 对每个块中的元素进行排序
    for (int i = 0; i < blockCount; i++) {
        sort(blockElements[i].begin(), blockElements[i].end());
    }
}

// 更新操作：将位置pos的值更新为val
void update(int pos, int val) {
    int blockId = block[pos];
    int oldVal = arr[pos];
    arr[pos] = val;
    
    // 从块中移除旧值
    for (auto it = blockElements[blockId].begin(); it != blockElements[blockId].end(); ++it) {
        if (*it == oldVal) {
            blockElements[blockId].erase(it);
            break;
        }
    }
    // 向块中添加新值
    blockElements[blockId].push_back(val);
    // 重新排序
    sort(blockElements[blockId].begin(), blockElements[blockId].end());
}

// 查询操作：查询区间[l,r]内大于等于k的元素个数
int query(int l, int r, int k) {
    int leftBlock = block[l];
    int rightBlock = block[r];
    int result = 0;
    
    if (leftBlock == rightBlock) {
        // 所有元素都在同一个块内，直接暴力查询
        for (int i = l; i <= r; i++) {
            if (arr[i] >= k) {
                result++;
            }
        }
    } else {
        // 处理左边不完整的块
        for (int i = l; i < (leftBlock + 1) * blen + 1 && i <= n; i++) {
            if (arr[i] >= k) {
                result++;
            }
        }
        
        // 处理中间完整的块，使用二分查找
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            // 使用二分查找找到第一个大于等于k的位置
            auto it = lower_bound(blockElements[i].begin(), blockElements[i].end(), k);
            // 计算大于等于k的元素个数
            result += blockElements[i].end() - it;
        }
        
        // 处理右边不完整的块
        for (int i = rightBlock * blen + 1; i <= r; i++) {
            if (arr[i] >= k) {
                result++;
            }
        }
    }
    
    return result;
}

int main() {
    // 读取输入
    scanf("%d", &n);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 初始化分块结构
    init();
    
    scanf("%d", &q);
    
    for (int i = 0; i < q; i++) {
        char op[2];
        scanf("%s", op);
        
        if (op[0] == 'C') {
            // 更新操作
            int x, y;
            scanf("%d%d", &x, &y);
            update(x, y);
        } else if (op[0] == 'Q') {
            // 查询操作
            int l, r, k;
            scanf("%d%d%d", &l, &r, &k);
            int result = query(l, r, k);
            printf("%d\n", result);
        }
    }
    
    return 0;
}

/*
时间复杂度分析：
- 初始化：O(n * sqrt(n))，因为需要对每个块进行排序
- 更新操作：
  - 对于不完整的块：O(1)（直接修改原始数组）
  - 对于完整块的排序：O(sqrt(n))（需要重新排序）
  - 总体时间复杂度：O(sqrt(n))
- 查询操作：
  - 对于不完整的块：O(sqrt(n))（直接遍历）
  - 对于完整块：O(log(sqrt(n)))（二分查找）
  - 总体时间复杂度：O(sqrt(n) * log(sqrt(n)))

空间复杂度分析：
- 原始数组：O(n)
- 块分配数组：O(n)
- 块元素列表：O(n)
- 总体空间复杂度：O(n)

优化说明：
1. 使用分块算法将数组分成大小为sqrt(n)的块
2. 对每个块中的元素进行排序，便于二分查找
3. 更新操作时，需要重新排序对应的块
4. 查询操作时，对不完整的块直接遍历，对完整的块使用二分查找

算法说明：
GIVEAWAY问题要求支持区间更新和区间查询，可以使用分块算法解决：
1. 将数组分成大小为sqrt(n)的块
2. 对每个块中的元素进行排序
3. 更新操作时，修改原始数组并重新排序对应的块
4. 查询操作时，对不完整的块直接遍历，对完整的块使用二分查找统计大于等于k的元素个数

与其他方法的对比：
- 暴力法：更新O(1)，查询O(n)，总时间复杂度O(q * n)
- 线段树：更新O(log n)，查询O(log n)，但实现复杂
- 分块算法：更新O(sqrt(n))，查询O(sqrt(n) * log(sqrt(n)))，实现相对简单

工程化考虑：
1. 使用scanf和printf提高输入输出效率
2. 使用lower_bound进行二分查找
3. 对于频繁更新的场景，可以考虑使用懒惰重建策略
4. 注意边界条件的处理
*/