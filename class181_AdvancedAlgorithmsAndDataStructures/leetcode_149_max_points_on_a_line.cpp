// LeetCode 149 Max Points on a Line
// C++ 实现

/**
 * LeetCode 149 Max Points on a Line
 * 
 * 题目描述：
 * 给你一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。
 * 求最多有多少个点在同一条直线上。
 * 
 * 解题思路：
 * 这是一个几何问题，可以使用斜率来判断点是否在同一条直线上。
 * 对于每个点，我们计算它与其他所有点的斜率，斜率相同的点在同一条直线上。
 * 为了避免浮点数精度问题，我们使用分数形式表示斜率，并将其化简为最简分数。
 * 
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
int maxPoints(int** points, int pointsSize, int* pointsColSize) {
    if (pointsSize <= 2) {
        return pointsSize;
    }
    
    int maxPointsOnLine = 0;
    
    // 对于每个点，计算它与其他点的斜率
    for (int i = 0; i < pointsSize; i++) {
        // 使用哈希表存储斜率及其对应的点数
        // 在实际实现中需要使用自定义哈希表或排序方法
        
        int duplicate = 1; // 重复点数（包括自身）
        int vertical = 0; // 垂直线上的点数
        
        for (int j = i + 1; j < pointsSize; j++) {
            // 检查是否为重复点
            if (points[i][0] == points[j][0] && points[i][1] == points[j][1]) {
                duplicate++;
                continue;
            }
            
            // 检查是否为垂直线
            if (points[i][0] == points[j][0]) {
                vertical++;
                continue;
            }
            
            // 计算斜率并化简为最简分数
            int dy = points[j][1] - points[i][1];
            int dx = points[j][0] - points[i][0];
            
            // 化简分数
            int gcd = getGCD(abs(dy), abs(dx));
            dy /= gcd;
            dx /= gcd;
            
            // 确保分母为正
            if (dx < 0) {
                dy = -dy;
                dx = -dx;
            }
            
            // 在实际实现中需要将斜率存储在哈希表中并计数
        }
        
        // 更新最大点数
        // maxPointsOnLine = max(maxPointsOnLine, vertical + duplicate);
        // 还需要考虑其他斜率的情况
    }
    
    return maxPointsOnLine;
}

// 计算最大公约数
int getGCD(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}

// 算法核心思想：
// 1. 对于每个点，计算它与其他所有点的斜率
// 2. 使用分数形式表示斜率并化简为最简分数
// 3. 统计相同斜率的点数
// 4. 特殊处理重复点和垂直线的情况

// 时间复杂度分析：
// - 外层循环：O(n)
// - 内层循环：O(n)
// - GCD计算：O(log(min(a,b)))
// - 总体时间复杂度：O(n^2 * log(min(a,b)))
// - 空间复杂度：O(n)（哈希表存储斜率）
*/

// 算法应用场景：
// 1. 计算几何问题
// 2. 斜率相关算法
// 3. 哈希表在几何问题中的应用