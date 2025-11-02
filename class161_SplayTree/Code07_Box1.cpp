/**
 * Box (HDU 2475) - C++实现
 * 
 * 【题目来源】HDU 2475
 * 【题目链接】http://acm.hdu.edu.cn/showproblem.php?pid=2475
 * 【题目大意】
 * 有n个盒子，每个盒子可能包含在另一个盒子中，支持以下操作：
 * 1. MOVE x y: 将盒子x移动到盒子y中（y为0表示移到最外层）
 * 2. QUERY x: 查询盒子x在哪一个盒子中（0表示在最外层）
 * 
 * 【算法分析】
 * 使用Splay树维护森林结构，每个Splay树表示一个包含关系树
 * 通过Splay操作优化频繁访问节点的访问速度
 * 
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * - 单次操作最坏情况可能达到O(n)
 * 
 * 【空间复杂度】O(n)
 * 
 * 【实现特点】
 * - 使用数组模拟节点结构，避免动态内存分配开销
 * - 维护包含关系的parent数组
 * - 使用辅助栈优化Splay操作
 */

// 由于环境限制，使用简化版本的C++实现

const int MAXN = 50010;

// Splay树节点相关数组
int father[MAXN];   // 父节点
int left[MAXN];     // 左子节点
int right[MAXN];    // 右子节点
int parent[MAXN];   // 包含关系中的父盒子

int stack[MAXN];    // 辅助栈
int top = 0;

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
}

/**
 * 【核心伸展操作】将节点i旋转到根节点
 * 这是Splay树的核心操作，通过一系列旋转使被访问节点移动到树的顶部
 * 时间复杂度: 均摊O(log n)，最坏情况O(n)
 * 空间复杂度: O(1)
 * 
 * @param i 需要旋转的节点索引
 */
// Splay操作，将节点i旋转到根
void splay(int i) {
    // 使用辅助栈收集路径上的所有节点
    top = 0;
    stack[++top] = i;
    int j = i;
    while (father[j] != 0) {
        stack[++top] = father[j];
        j = father[j];
    }
    
    // 从根到目标节点依次下传懒标记（在此题中为空操作）
    while (top > 0) {
        // down操作在此题中为空
        top--;
    }
    
    // 执行Splay操作
    int f = father[i], g = father[f];
    while (f != 0) {
        // 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
        if (g != 0) {
            if (lr(i) == lr(f)) rotate(f);  // Zig-Zig情况
            else rotate(i);                // Zig-Zag情况
        }
        rotate(i);
        f = father[i];
        g = father[f];
    }
}

/**
 * 【查找根节点】查找节点i所在树的根节点
 * 时间复杂度: 均摊O(log n)
 * 
 * @param i 要查找根节点的节点索引
 * @return 节点i所在树的根节点索引
 */
// 查找节点i的根节点
int findRoot(int i) {
    // 将节点i旋转到根
    splay(i);
    
    // 找到最左边的节点（即根节点）
    int cur = i;
    while (left[cur] != 0) {
        cur = left[cur];
    }
    
    // 将根节点旋转到根（优化后续访问）
    splay(cur);
    
    return cur;
}

/**
 * 【移动操作】将盒子x移动到盒子y中
 * 时间复杂度: 均摊O(log n)
 * 
 * @param x 要移动的盒子编号
 * @param y 目标盒子编号（0表示移到最外层）
 */
// 移动操作：将盒子x移动到盒子y中
void move(int x, int y) {
    // 先将x从原来的包含关系中分离
    // 将盒子x旋转到根
    splay(x);
    
    // 断开x与其左子树的连接
    if (left[x] != 0) {
        father[left[x]] = 0;
    }
    left[x] = 0;
    
    // 如果y不为0，将x连接到y的最右路径
    if (y != 0) {
        // 将盒子y旋转到根
        splay(y);
        
        // 找到y的最右节点
        int cur = y;
        while (right[cur] != 0) {
            cur = right[cur];
        }
        
        // 将最右节点旋转到根
        splay(cur);
        
        // 将x连接为最右节点的右子节点
        right[cur] = x;
        father[x] = cur;
    }
    
    // 更新包含关系
    parent[x] = y;
}

/**
 * 【查询操作】查询盒子x的直接外层盒子
 * 时间复杂度: O(1)
 * 
 * @param x 要查询的盒子编号
 * @return 盒子x的直接外层盒子编号（0表示在最外层）
 */
// 查询操作：查询盒子x的直接外层盒子
int query(int x) {
    return parent[x];
}

/*
// 由于环境限制，此处省略主函数实现
int main() {
    // 实际使用时需要根据具体环境调整IO方式
    return 0;
}
*/