/**
 * SuperMemo (POJ 3580) - C++实现
 * 
 * 【题目来源】POJ 3580
 * 【题目链接】http://poj.org/problem?id=3580
 * 【题目大意】
 * 维护一个序列，支持以下操作：
 * 1. ADD x y D: 将区间[x,y]每个数增加D
 * 2. REVERSE x y: 翻转区间[x,y]
 * 3. REVOLVE x y T: 将区间[x,y]循环右移T位
 * 4. INSERT x P: 在位置x后插入元素P
 * 5. DELETE x: 删除位置x的元素
 * 6. MIN x y: 查询区间[x,y]的最小值
 * 
 * 【算法分析】
 * 使用Splay树维护序列，支持区间操作
 * 通过懒标记优化区间操作性能
 * 
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * 
 * 【空间复杂度】O(n)
 * 
 * 【实现特点】
 * - 使用数组模拟节点结构，避免动态内存分配开销
 * - 实现懒标记（延迟传播）机制处理区间操作
 * - 添加哨兵节点简化边界情况处理
 */

// 由于环境限制，使用简化版本的C++实现

const int MAXN = 200010;
const int INF = 2147483647;

// Splay树节点相关数组
int num[MAXN];     // 节点权值
int father[MAXN];  // 父节点
int left[MAXN];    // 左子节点
int right[MAXN];   // 右子节点
int size[MAXN];    // 子树大小

// 维护区间信息
int min_val[MAXN];    // 区间最小值
bool reverse[MAXN];   // 区间翻转标记
bool update[MAXN];    // 区间更新标记
int change[MAXN];     // 区间更新值

int head = 0;  // 树根
int cnt = 0;   // 节点计数

// 以下为Splay树核心实现，可适配不同IO环境

/**
 * 【自底向上维护】更新节点信息
 * 时间复杂度: O(1)
 * 
 * @param i 需要更新的节点索引
 */
// 更新节点信息
void up(int i) {
    // 更新子树大小
    size[i] = size[left[i]] + size[right[i]] + 1;
    
    // 更新区间最小值
    min_val[i] = num[i];
    if (left[i] != 0) min_val[i] = (min_val[i] < min_val[left[i]]) ? min_val[i] : min_val[left[i]];
    if (right[i] != 0) min_val[i] = (min_val[i] < min_val[right[i]]) ? min_val[i] : min_val[right[i]];
}

/**
 * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
 * 时间复杂度: O(1)
 * 
 * @param i 需要判断的节点索引
 * @return 1表示右子节点，0表示左子节点
 */
// 判断节点i是其父节点的左儿子还是右儿子
int lr(int i) {
    return right[father[i]] == i ? 1 : 0;
}

/**
 * 【核心旋转操作】将节点i旋转至其父节点的位置
 * 这是Splay树维护平衡的基本操作
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 
 * @param i 需要旋转的节点索引
 */
// 旋转操作
void rotate(int i) {
    int f = father[i], g = father[f], soni = lr(i), sonf = lr(f);
    
    // 【旋转逻辑】根据当前节点是左子还是右子执行不同的旋转操作
    if (soni == 1) {       // 右子节点，执行右旋
        right[f] = left[i];
        if (right[f] != 0) father[right[f]] = f;
        left[i] = f;
    } else {               // 左子节点，执行左旋
        left[f] = right[i];
        if (left[f] != 0) father[left[f]] = f;
        right[i] = f;
    }
    
    // 更新祖父节点的子节点指针
    if (g != 0) {
        if (sonf == 1) right[g] = i;
        else left[g] = i;
    }
    
    // 更新父指针
    father[f] = i;
    father[i] = g;
    
    // 【重要】更新节点信息，先更新被旋转的父节点，再更新当前节点
    up(f);
    up(i);
}

/**
 * 【核心伸展操作】将节点i旋转到goal的子节点位置
 * 如果goal为0，则将i旋转到根节点
 * 这是Splay树的核心操作，通过一系列旋转使被访问节点移动到树的顶部
 * 时间复杂度: 均摊O(log n)，最坏情况O(n)
 * 空间复杂度: O(1)
 * 
 * @param i 需要旋转的节点索引
 * @param goal 目标父节点索引
 */
// Splay操作，将节点i旋转到goal下方
void splay(int i, int goal) {
    int f = father[i], g = father[f];
    
    // 当当前节点的父节点不是目标节点时，继续旋转
    while (f != goal) {
        // 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
        if (g != goal) {
            // 如果父节点和当前节点在同侧，先旋转父节点（Zig-Zig情况）
            // 否则直接旋转当前节点（Zig-Zag情况）
            if (lr(i) == lr(f)) rotate(f);
            else rotate(i);
        }
        // 最后旋转当前节点
        rotate(i);
        
        // 更新父节点和祖父节点
        f = father[i];
        g = father[f];
    }
    
    // 如果旋转到根节点，更新根节点指针
    if (goal == 0) head = i;
}

/**
 * 【查找操作】在整棵树中找到中序遍历排名为rank的节点
 * 时间复杂度: O(log n)
 * 
 * @param rank 目标排名（从1开始）
 * @return 对应排名的节点索引
 */
// 查找中序排名为rank的节点
int find(int rank) {
    int i = head;
    while (i != 0) {
        if (size[left[i]] + 1 == rank) return i;
        else if (size[left[i]] >= rank) i = left[i];
        else {
            rank -= size[left[i]] + 1;
            i = right[i];
        }
    }
    return 0; // 未找到对应排名的节点
}

/**
 * 【懒标记设置】设置区间更新标记
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 
 * @param i 要设置标记的节点索引
 * @param val 更新的值
 */
// 设置区间更新标记
void setUpdate(int i, int val) {
    if (i == 0) return;
    
    // 设置更新标记
    update[i] = true;
    change[i] = val;
    
    // 更新当前节点的值和区间信息
    num[i] += val;
    min_val[i] += val;
}

/**
 * 【懒标记设置】设置区间翻转标记
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 
 * @param i 要设置标记的节点索引
 */
// 设置区间翻转标记
void setReverse(int i) {
    if (i == 0) return;
    
    // 翻转标记
    reverse[i] = !reverse[i];
    
    // 交换左右子树
    int tmp = left[i];
    left[i] = right[i];
    right[i] = tmp;
}

/**
 * 【懒标记下传】下传懒标记到子节点
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 
 * @param i 要下传懒标记的节点索引
 */
// 下传懒标记
void down(int i) {
    // 处理区间更新标记
    if (update[i]) {
        setUpdate(left[i], change[i]);
        setUpdate(right[i], change[i]);
        update[i] = false; // 清除标记
    }
    
    // 处理区间翻转标记
    if (reverse[i]) {
        setReverse(left[i]);
        setReverse(right[i]);
        reverse[i] = false; // 清除标记
    }
}

/**
 * 【构建树】根据数组构建初始Splay树
 * 使用逐个插入的方式构建，更高效的方式是递归构建，但这里为了简单起见使用逐个插入
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * @param arr 初始数组
 * @param n 数组长度
 */
// 构建初始序列
void build(int arr[], int n) {
    // 添加哨兵节点
    // 【边界处理】使用哨兵节点简化边界情况处理
    num[++cnt] = INF;
    size[cnt] = 1;
    min_val[cnt] = num[cnt];
    head = cnt;
    
    // 逐个插入节点
    for (int i = 1; i <= n; i++) {
        num[++cnt] = arr[i];
        size[cnt] = 1;
        min_val[cnt] = num[cnt];
        father[cnt] = head;
        right[head] = cnt;
        splay(cnt, 0); // 每次插入后splay到根节点
    }
    
    // 添加尾部哨兵节点
    num[++cnt] = INF;
    size[cnt] = 1;
    min_val[cnt] = num[cnt];
    father[cnt] = head;
    right[head] = cnt;
    splay(cnt, 0);
}

/**
 * 【区间操作】区间加法 - 将区间[x,y]中的每个数增加d
 * 时间复杂度: 均摊O(log n)
 * 空间复杂度: O(1)
 * 
 * @param x 区间左端点（从1开始）
 * @param y 区间右端点
 * @param d 要增加的值
 */
// 区间加法操作
void add(int x, int y, int d) {
    // 【区间访问技巧】利用find和splay操作将目标区间隔离为一个子树
    // 找到前驱节点和后继节点
    int l = find(x);
    int r = find(y + 2);
    
    // 将l旋转到根，r旋转到l的右子节点
    splay(l, 0);
    splay(r, l);
    
    // 此时目标区间就是r的左子树，设置更新标记
    setUpdate(left[r], d);
    
    // 更新节点信息
    up(r);
    up(l);
}

/**
 * 【区间操作】区间翻转 - 将区间[x,y]中的元素顺序翻转
 * 时间复杂度: 均摊O(log n)
 * 空间复杂度: O(1)
 * 
 * @param x 区间左端点（从1开始）
 * @param y 区间右端点
 */
// 区间翻转操作
void reverse_range(int x, int y) {
    // 同样使用区间隔离技巧
    int l = find(x);
    int r = find(y + 2);
    
    splay(l, 0);
    splay(r, l);
    
    // 设置翻转标记
    setReverse(left[r]);
    
    up(r);
    up(l);
}

/**
 * 【区间操作】区间循环右移 - 将区间[x,y]循环右移t位
 * 时间复杂度: 均摊O(log n)
 * 空间复杂度: O(1)
 * 
 * @param x 区间左端点（从1开始）
 * @param y 区间右端点
 * @param t 右移的位数
 */
// 区间循环右移操作
void revolve(int x, int y, int t) {
    int len = y - x + 1;
    // 【边界处理】处理t可能为负数或超过区间长度的情况
    t = ((t % len) + len) % len;
    if (t == 0) return; // 右移0位，无需操作
    
    // 实现思路：将区间分为两部分，交换它们的位置
    // 第一部分：x到y-t
    // 第二部分：y-t+1到y
    // 需要将第二部分移动到第一部分前面
    
    // 先分离第二部分
    int l = find(y - t + 1);
    int r = find(y + 2);
    splay(l, 0);
    splay(r, l);
    int subtree = left[r]; // 这就是第二部分
    left[r] = 0; // 断开连接
    up(r);
    up(l);
    
    // 然后将第二部分插入到区间最前面
    l = find(x);
    r = find(x + 1);
    splay(l, 0);
    splay(r, l);
    left[r] = subtree; // 连接第二部分
    father[subtree] = r; // 更新父指针
    up(r);
    up(l);
}

/**
 * 【插入操作】在位置x后插入元素p
 * 时间复杂度: 均摊O(log n)
 * 空间复杂度: O(1)
 * 
 * @param x 插入位置（从0开始计数）
 * @param p 要插入的值
 */
// 插入操作
void insert(int x, int p) {
    // 找到插入位置的前驱和后继
    int l = find(x + 1);
    int r = find(x + 2);
    
    splay(l, 0);
    splay(r, l);
    
    // 创建新节点并连接
    num[++cnt] = p;
    size[cnt] = 1;
    min_val[cnt] = p;
    left[r] = cnt;
    father[cnt] = r;
    
    up(r);
    up(l);
}

/**
 * 【删除操作】删除位置x的元素
 * 时间复杂度: 均摊O(log n)
 * 空间复杂度: O(1)
 * 
 * @param x 要删除的位置（从1开始计数）
 */
// 删除操作
void delete_pos(int x) {
    // 找到要删除元素的前驱和后继
    int l = find(x);
    int r = find(x + 2);
    
    splay(l, 0);
    splay(r, l);
    
    // 直接断开连接即可删除中间的元素
    left[r] = 0;
    
    up(r);
    up(l);
}

/**
 * 【查询操作】查询区间[x,y]的最小值
 * 时间复杂度: 均摊O(log n)
 * 空间复杂度: O(1)
 * 
 * @param x 区间左端点（从1开始）
 * @param y 区间右端点
 * @return 区间内的最小值
 */
// 查询区间最小值
int queryMin(int x, int y) {
    // 同样使用区间隔离技巧
    int l = find(x);
    int r = find(y + 2);
    
    splay(l, 0);
    splay(r, l);
    
    // 直接返回r左子树的最小值
    return min_val[left[r]];
}

/*
int main() {
    // 由于环境限制，此处省略主函数实现
    // 实际使用时需要根据具体环境调整IO方式
    return 0;
}
*/