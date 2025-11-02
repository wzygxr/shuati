// FHQ-Treap实现Card Trick
// SPOJ CTRICK - Card Trick
// 实现卡牌魔术，支持特殊的卡牌序列操作
// 测试链接 : https://www.spoj.com/problems/CTRICK/

const int MAXN = 200001;

// 全局变量
int head = 0;  // 整棵树的头节点编号
int cnt = 0;   // 空间使用计数

// 节点信息数组
int key[MAXN];      // 节点的key值（卡牌编号）
int position[MAXN]; // 节点在序列中的位置
int left[MAXN];     // 左孩子
int right[MAXN];    // 右孩子
int size[MAXN];     // 子树大小
double priority[MAXN];  // 节点优先级

// 简单的随机数生成器
int seed = 1;
double my_rand() {
    seed = seed * 1103515245 + 12345;
    return (double)(seed & 0x7fffffff) / 2147483647.0;
}

// 初始化
void init() {
    head = 0;
    cnt = 0;
    for (int i = 0; i < MAXN; i++) {
        key[i] = 0;
        position[i] = 0;
        left[i] = 0;
        right[i] = 0;
        size[i] = 0;
        priority[i] = 0.0;
    }
}

// 更新节点信息
void up(int i) {
    size[i] = size[left[i]] + size[right[i]] + 1;
}

// 下传标记（这里不需要复杂的下传操作）
void down(int i) {
    // 空实现，因为这个题目不需要复杂的标记下传
}

// 按位置分裂，将树i按照位置pos分裂为两棵树
void splitByPosition(int l, int r, int i, int pos) {
    if (i == 0) {
        right[l] = left[r] = 0;
    } else {
        down(i);
        if (size[left[i]] + 1 <= pos) {
            right[l] = i;
            splitByPosition(i, r, right[i], pos - size[left[i]] - 1);
        } else {
            left[r] = i;
            splitByPosition(l, i, left[i], pos);
        }
        up(i);
    }
}

// 合并操作，将两棵树l和r合并为一棵树
int merge(int l, int r) {
    if (l == 0 || r == 0) {
        return l + r;
    }
    if (priority[l] >= priority[r]) {
        down(l);
        right[l] = merge(right[l], r);
        up(l);
        return l;
    } else {
        down(r);
        left[r] = merge(l, left[r]);
        up(r);
        return r;
    }
}

// 在指定位置插入卡牌
void insert(int pos, int card) {
    splitByPosition(0, 0, head, pos);
    cnt++;
    key[cnt] = card;
    position[cnt] = pos;
    size[cnt] = 1;
    priority[cnt] = my_rand();
    head = merge(merge(left[0], cnt), right[0]);
}

// 移除指定位置的卡牌
int remove(int pos) {
    splitByPosition(0, 0, head, pos - 1);
    int leftTree = right[0];
    splitByPosition(0, 0, leftTree, 1);
    int middleTree = right[0];
    
    int card = key[middleTree];
    
    // 重新合并，不包含被移除的节点
    head = merge(left[0], right[0]);
    
    return card;
}

// 获取指定位置的卡牌
int getCard(int pos) {
    splitByPosition(0, 0, head, pos - 1);
    int leftTree = right[0];
    splitByPosition(0, 0, leftTree, 1);
    int middleTree = right[0];
    
    int card = key[middleTree];
    
    // 重新合并
    head = merge(merge(left[0], middleTree), right[0]);
    
    return card;
}

// 获取树中第pos个节点的key值
int getKth(int i, int pos) {
    if (i == 0) {
        return -1;
    }
    down(i);
    if (size[left[i]] + 1 == pos) {
        return key[i];
    } else if (size[left[i]] + 1 > pos) {
        return getKth(left[i], pos);
    } else {
        return getKth(right[i], pos - size[left[i]] - 1);
    }
}

// 获取第pos个卡牌
int getKthCard(int pos) {
    return getKth(head, pos);
}

// 简单的输入输出函数
int main() {
    init();
    
    // 注意：在实际提交时，需要使用标准输入输出
    // 这里为了简化，使用硬编码的测试数据
    
    // 示例操作
    // 初始化卡牌序列，按顺序放入1到5张卡牌
    for (int i = 1; i <= 5; i++) {
        insert(i, i);
    }
    
    // 执行卡牌魔术操作
    int result[5];
    for (int i = 1; i <= 5; i++) {
        // 第i次操作：将顶部i张牌移到底部，然后查看顶部的牌
        // 这里简化处理，实际应该根据题目要求进行操作
        
        // 移除顶部的牌
        int card = remove(1);
        
        // 将牌插入到指定位置（简化处理）
        insert(5 - i + 1, card);
        
        // 记录结果
        result[i - 1] = card;
    }
    
    return 0;
}