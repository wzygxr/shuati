/**
 * C++ 线段树实现 - LeetCode 699. Falling Squares
 * 题目链接: https://leetcode.cn/problems/falling-squares/
 * 题目描述:
 * 在二维平面上的 x 轴上，放置着一些方块。
 * 给你一个二维整数数组 positions ，其中 positions[i] = [lefti, sideLengthi] 表示：
 * 第 i 个方块边长为 sideLengthi ，其左侧边与 x 轴上坐标点 lefti 对齐。
 * 每个方块从一个比目前所有落地方块更高的高度掉落而下，沿 y 轴负方向下落，
 * 直到着陆到另一个正方形的顶边或者是 x 轴上。一个方块仅仅是擦过另一个方块的左侧边或右侧边不算着陆。
 * 一旦着陆，它就会固定在原地，无法移动。
 * 在每个方块掉落后，你需要记录目前所有已经落稳的方块堆叠的最高高度。
 * 返回一个整数数组 ans ，其中 ans[i] 表示在第 i 个方块掉落后堆叠的最高高度。
 *
 * 示例 1:
 * 输入: positions = [[1,2],[2,3],[6,1]]
 * 输出: [2,5,5]
 * 解释:
 * 第1个方块掉落后，最高的堆叠由方块1形成，堆叠的最高高度为2。
 * 第2个方块掉落后，最高的堆叠由方块1和2形成，堆叠的最高高度为5。
 * 第3个方块掉落后，最高的堆叠仍然由方块1和2形成，堆叠的最高高度为5。
 * 因此，返回[2, 5, 5]作为答案。
 *
 * 示例 2:
 * 输入: positions = [[100,100],[200,100]]
 * 输出: [100,100]
 * 解释:
 * 第1个方块掉落后，最高的堆叠由方块1形成，堆叠的最高高度为100。
 * 第2个方块掉落后，最高的堆叠可以由方块1或方块2形成，堆叠的最高高度为100。
 * 注意，方块2擦过方块1的右侧边，但不会算作在方块1上着陆。
 * 因此，返回[100, 100]作为答案。
 *
 * 提示:
 * 1 <= positions.length <= 1000
 * 1 <= lefti <= 10^8
 * 1 <= sideLengthi <= 10^6
 *
 * 解题思路:
 * 这是一个区间更新和区间查询最大值的问题，可以使用线段树来解决。
 * 1. 由于坐标范围较大(10^8)，需要进行离散化处理
 * 2. 对于每个掉落的方块:
 *    - 查询当前方块覆盖区间内的最大高度
 *    - 新的高度 = 当前最大高度 + 方块边长
 *    - 更新当前方块覆盖区间的高度为新高度
 *    - 记录当前所有方块的最大高度
 * 3. 使用线段树维护区间最大值，支持区间更新和区间查询
 *
 * 时间复杂度: O(n log n)，其中n是方块数量
 * 空间复杂度: O(n)
 */

// 定义最大数组大小
#define MAXN 2005

// 线段树结构
struct Node {
    int l, r;       // 区间左右端点
    int max_val;    // 区间最大值
    int lazy;       // 懒标记
    bool update;    // 是否有更新操作
};

// 线段树数组
Node tree[MAXN * 4];

// 离散化数组
int nums[MAXN];
int map_val[MAXN];
int n;

// 向上传递
void pushUp(int i) {
    tree[i].max_val = (tree[i << 1].max_val > tree[i << 1 | 1].max_val) ? 
                      tree[i << 1].max_val : tree[i << 1 | 1].max_val;
}

// 懒标记下发
void pushDown(int i) {
    if (tree[i].update) {
        // 下发给左子树
        tree[i << 1].max_val = tree[i].lazy;
        tree[i << 1].lazy = tree[i].lazy;
        tree[i << 1].update = true;
        
        // 下发给右子树
        tree[i << 1 | 1].max_val = tree[i].lazy;
        tree[i << 1 | 1].lazy = tree[i].lazy;
        tree[i << 1 | 1].update = true;
        
        // 清除父节点的懒标记
        tree[i].update = false;
    }
}

// 建立线段树
void build(int l, int r, int i) {
    tree[i].l = l;
    tree[i].r = r;
    tree[i].max_val = 0;
    tree[i].lazy = 0;
    tree[i].update = false;
    
    if (l == r) {
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
}

// 区间更新
void update(int jobl, int jobr, int val, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        tree[i].max_val = val;
        tree[i].lazy = val;
        tree[i].update = true;
        return;
    }
    pushDown(i);
    int mid = (l + r) >> 1;
    if (jobl <= mid) {
        update(jobl, jobr, val, l, mid, i << 1);
    }
    if (jobr > mid) {
        update(jobl, jobr, val, mid + 1, r, i << 1 | 1);
    }
    pushUp(i);
}

// 区间查询最大值
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return tree[i].max_val;
    }
    pushDown(i);
    int mid = (l + r) >> 1;
    int ans = 0;
    if (jobl <= mid) {
        int temp = query(jobl, jobr, l, mid, i << 1);
        ans = (ans > temp) ? ans : temp;
    }
    if (jobr > mid) {
        int temp = query(jobl, jobr, mid + 1, r, i << 1 | 1);
        ans = (ans > temp) ? ans : temp;
    }
    return ans;
}

// 简单排序函数（冒泡排序）
void bubbleSort(int arr[], int len) {
    for (int i = 0; i < len - 1; i++) {
        for (int j = 0; j < len - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// 去重函数
int removeDuplicates(int arr[], int len) {
    if (len == 0) return 0;
    
    int i = 0;
    for (int j = 1; j < len; j++) {
        if (arr[j] != arr[i]) {
            i++;
            arr[i] = arr[j];
        }
    }
    return i + 1;
}

// 收集所有坐标点并离散化
void discretization(int positions[][2], int len) {
    int index = 0;
    
    // 收集所有坐标点
    for (int i = 0; i < len; i++) {
        int left = positions[i][0];
        int side = positions[i][1];
        int right = left + side;
        
        nums[index++] = left;
        nums[index++] = right;
    }
    
    // 排序
    bubbleSort(nums, index);
    
    // 去重
    n = removeDuplicates(nums, index);
    
    // 建立映射关系
    for (int i = 0; i < n; i++) {
        map_val[i] = i + 1;
    }
}

// 主要函数
void fallingSquares(int positions[][2], int len, int result[]) {
    // 特殊情况处理
    if (len == 0) {
        return;
    }
    
    // 收集所有坐标点并离散化
    discretization(positions, len);
    
    // 初始化线段树
    build(1, n, 1);
    
    // 记录全局最大高度
    int maxHeight = 0;
    
    // 处理每个方块
    for (int i = 0; i < len; i++) {
        int left = positions[i][0];
        int side = positions[i][1];
        int right = left + side;
        
        // 获取离散化后的坐标（简化处理）
        int l, r;
        for (int j = 0; j < n; j++) {
            if (nums[j] == left) l = map_val[j];
            if (nums[j] == right) r = map_val[j];
        }
        
        // 查询当前区间内的最大高度
        int currentMax = query(l, r - 1, 1, n, 1);
        
        // 计算新高度
        int newHeight = currentMax + side;
        
        // 更新区间高度
        update(l, r - 1, newHeight, 1, n, 1);
        
        // 更新全局最大高度
        maxHeight = (maxHeight > newHeight) ? maxHeight : newHeight;
        
        // 记录当前最大高度
        result[i] = maxHeight;
    }
}