// 水壶问题
// 有两个容量分别为 x 升 和 y 升 的水壶以及无限多的水。
// 请判断能否通过使用这两个水壶，从而可以得到恰好 z 升 的水？
// 如果可以，最后请用以上水壶中的一或两个来盛放取得的 z 升 水。
// 你允许：
// 装满任意一个水壶
// 清空任意一个水壶
// 从一个水壶向另外一个水壶倒水，直到装满或者倒空
// 测试链接 : https://leetcode.cn/problems/water-and-jug-problem/
// 
// 算法思路：
// 使用BFS搜索所有可能的状态。每个状态用(a, b)表示，其中a是第一个水壶的水量，b是第二个水壶的水量。
// 通过六种操作生成新的状态：装满A、装满B、倒空A、倒空B、A倒向B、B倒向A。
// 
// 时间复杂度：O(x * y)，状态空间为x*y
// 空间复杂度：O(x * y)，用于存储访问状态
// 
// 工程化考量：
// 1. 状态表示：使用数组记录访问状态
// 2. 操作模拟：精确模拟六种倒水操作
// 3. 数学优化：使用贝祖定理（裴蜀定理）进行数学判断
// 4. 边界情况：z为0，z大于x+y等特殊情况

#define MAXN 100005

// 简单的队列实现
int queue[MAXN][2];  // 存储状态 (a, b)
int head, tail;

// 简单的哈希表实现，用于记录访问状态
int visited[MAXN];  // 使用哈希函数将二维状态映射为一维

// 哈希函数：将二维状态映射为一维
int hash(int a, int b) {
    return (a * 1000000 + b) % MAXN;
}

// 计算最大公约数（欧几里得算法）
int gcd(int a, int b) {
    if (b == 0) return a;
    return gcd(b, a % b);
}

// 使用数学方法判断：贝祖定理
int canMeasureWater(int x, int y, int z) {
    // 边界情况处理
    if (z == 0) return 1;
    if (z > x + y) return 0;
    if (x == 0 && y == 0) return z == 0;
    
    // 使用数学方法判断：贝祖定理
    // z必须是x和y的最大公约数的倍数
    int gcd_val = gcd(x, y);
    return z % gcd_val == 0;
}

// BFS版本：用于理解和验证小规模数据
int canMeasureWaterBFS(int x, int y, int z) {
    if (z == 0) return 1;
    if (z > x + y) return 0;
    if (x == 0 && y == 0) return z == 0;
    
    // 初始化队列和访问状态
    head = tail = 0;
    int i;
    for (i = 0; i < MAXN; i++) {
        visited[i] = 0;
    }
    
    queue[tail][0] = 0;
    queue[tail][1] = 0;
    tail++;
    visited[hash(0, 0)] = 1;
    
    while (head < tail) {
        int a = queue[head][0];
        int b = queue[head][1];
        head++;
        
        // 检查是否达到目标
        if (a == z || b == z || a + b == z) {
            return 1;
        }
        
        // 生成所有可能的操作
        int next_states[6][2];
        int next_count = 0;
        
        // 1. 装满A
        next_states[next_count][0] = x;
        next_states[next_count][1] = b;
        next_count++;
        
        // 2. 装满B
        next_states[next_count][0] = a;
        next_states[next_count][1] = y;
        next_count++;
        
        // 3. 倒空A
        next_states[next_count][0] = 0;
        next_states[next_count][1] = b;
        next_count++;
        
        // 4. 倒空B
        next_states[next_count][0] = a;
        next_states[next_count][1] = 0;
        next_count++;
        
        // 5. A倒向B
        int pour_ab = (a < y - b) ? a : (y - b);  // 可以倒出的水量
        next_states[next_count][0] = a - pour_ab;
        next_states[next_count][1] = b + pour_ab;
        next_count++;
        
        // 6. B倒向A
        int pour_ba = (b < x - a) ? b : (x - a);  // 可以倒出的水量
        next_states[next_count][0] = a + pour_ba;
        next_states[next_count][1] = b - pour_ba;
        next_count++;
        
        // 将未访问的状态加入队列
        for (i = 0; i < next_count; i++) {
            int ha = next_states[i][0];
            int hb = next_states[i][1];
            int h = hash(ha, hb);
            if (!visited[h]) {
                visited[h] = 1;
                queue[tail][0] = ha;
                queue[tail][1] = hb;
                tail++;
            }
        }
    }
    
    return 0;
}

// 优化版本：使用数学方法 + BFS小规模验证
int canMeasureWaterOptimized(int x, int y, int z) {
    // 数学方法快速判断
    if (z == 0) return 1;
    if (z > x + y) return 0;
    if (x == 0) return (z == y || z == 0);
    if (y == 0) return (z == x || z == 0);
    
    int gcd_val = gcd(x, y);
    if (z % gcd_val != 0) return 0;
    
    // 对于小规模数据，使用BFS验证
    if (x * y <= 1000000) {
        return canMeasureWaterBFS(x, y, z);
    }
    
    return 1;
}