// 阶梯博弈 (Staircase Nim)
// 有一个一维棋盘，有格子标号1,2,3,...,有n个棋子放在一些格子上
// 两人博弈，只能将棋子向左移，不能和其他棋子重叠，也不能跨越其他棋子
// 最后移动者胜，问先手是否必胜
// 
// 题目来源：
// 1. POJ 1704 Georgia and Bob - http://poj.org/problem?id=1704
// 2. HDU 1740 A New Stone Game - http://acm.hdu.edu.cn/showproblem.php?pid=1740
// 3. 牛客网 NC13685 取石子游戏 - https://www.nowcoder.com/practice/f6153503169545229c77481040056a63
// 
// 算法核心思想：
// 1. 阶梯博弈转换为尼姆博弈：将棋子两两配对，每对之间的空格数等效为尼姆博弈中的石子数
// 2. 当n为奇数时，将最左边的棋子与位置0绑定
// 
// 时间复杂度分析：
// 1. 排序：O(n log n) - 对棋子位置进行排序
// 2. 计算：O(n) - 计算配对间的空格数并求异或和
// 
// 空间复杂度分析：
// 1. 位置数组：O(n) - 存储棋子位置
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：利用排序和异或运算优化计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的棋盘大小和棋子数量

// 最大棋子数
const int MAXN = 1001;

// 棋子位置数组
int positions[MAXN];

// 简单排序函数（冒泡排序）
void sort(int* arr, int n) {
    int i, j;
    for (i = 0; i < n - 1; i++) {
        for (j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                // 交换元素
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// 
// 算法原理：
// 1. 阶梯博弈可以转换为尼姆博弈
// 2. 将棋子从右到左两两配对
// 3. 每对棋子之间的空格数等效为尼姆博弈中的一堆石子
// 4. 当n为奇数时，将最左边的棋子与位置0绑定
// 5. 计算所有堆石子数的异或和，不为0表示先手必胜
// 
// 转换原理：
// 在阶梯博弈中，每次移动棋子相当于将石子从一堆移动到另一堆
// 通过配对的方式，可以将问题转化为标准的尼姆博弈
int solve(int* positions, int n) {
    // 异常处理：处理非法输入
    if (n <= 0) {
        return 0; // 空游戏，先手败
    }
    
    // 对棋子位置进行排序
    sort(positions, n);
    
    // 计算异或和
    int xorSum = 0;
    
    // 当n为奇数时，将最左边的棋子与位置0绑定
    // 即计算positions[0] - 0 = positions[0]
    if ((n & 1) == 1) {
        xorSum ^= positions[0];
    }
    
    // 从右到左两两配对，计算每对之间的空格数
    int i;
    for (i = n - 2; i >= 0; i -= 2) {
        // 计算第i+1个棋子和第i个棋子之间的空格数
        // 空格数 = positions[i+1] - positions[i] - 1
        xorSum ^= (positions[i + 1] - positions[i] - 1);
    }
    
    // 异或和不为0表示先手必胜，为0表示先手必败
    return xorSum != 0 ? 1 : 0; // 1表示W，0表示L
}

// 测试示例
int main() {
    // 示例1: positions = {1, 3, 5}, n = 3
    // 排序后: {1, 3, 5}
    // n为奇数，xorSum = 1 ^ (5-3-1) = 1 ^ 1 = 0
    // 预期结果: 0 (L)
    int positions1[3] = {1, 3, 5};
    int result1 = solve(positions1, 3);
    
    // 示例2: positions = {2, 4, 6, 8}, n = 4
    // 排序后: {2, 4, 6, 8}
    // n为偶数，xorSum = (4-2-1) ^ (8-6-1) = 1 ^ 1 = 0
    // 预期结果: 0 (L)
    int positions2[4] = {2, 4, 6, 8};
    int result2 = solve(positions2, 4);
    
    // 示例3: positions = {1, 4, 7}, n = 3
    // 排序后: {1, 4, 7}
    // n为奇数，xorSum = 1 ^ (7-4-1) = 1 ^ 2 = 3
    // 预期结果: 1 (W)
    int positions3[3] = {1, 4, 7};
    int result3 = solve(positions3, 3);
    
    return 0;
}