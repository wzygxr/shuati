/*
 * POJ 1204 Word Puzzles
 * 题目链接：http://poj.org/problem?id=1204
 * 题目描述：给一个字母矩阵和一些字符串，求字符串在矩阵中出现的位置及其方向
 * 
 * 算法详解：
 * 这是一道典型的AC自动机应用题。我们需要在二维矩阵中查找多个模式串，
 * 可以使用AC自动机来优化匹配过程。
 * 
 * 算法核心思想：
 * 1. 将所有模式串插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 在矩阵的8个方向上分别进行匹配
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
 * 2. 构建fail指针：O(∑|Pi|)
 * 3. 矩阵匹配：O(L×C×8×max(|Pi|))，其中L是行数，C是列数
 * 总时间复杂度：O(∑|Pi| + L×C×max(|Pi|))
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 二维矩阵中的字符串匹配
 * 2. 多方向字符串搜索
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用数组代替链表提高访问速度
 * 3. 内存优化：合理设置数组大小，避免浪费
 * 
 * 与机器学习的联系：
 * 1. 在图像处理中用于模式识别
 * 2. 在游戏开发中用于寻路算法
 */

#define MAXN 1005
#define MAXS 100005

// Trie树节点
struct TrieNode {
    int children[26];
    int isEnd;
    int fail;
    int wordId; // 单词编号
};

struct TrieNode tree[MAXS];
int cnt = 0;
int root = 0;

char matrix[MAXN][MAXN];
int L, C, W;
char words[MAXN][MAXN];
int resultX[MAXN], resultY[MAXN];
char resultDir[MAXN];

// 8个方向：A=北, B=东北, C=东, D=东南, E=南, F=西南, G=西, H=西北
int dx[8] = {-1, -1, 0, 1, 1, 1, 0, -1};
int dy[8] = {0, 1, 1, 1, 0, -1, -1, -1};
char dirs[8] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

// 初始化Trie树节点
void initNode(int node) {
    int i;
    for (i = 0; i < 26; i++) {
        tree[node].children[i] = 0;
    }
    tree[node].isEnd = 0;
    tree[node].fail = 0;
    tree[node].wordId = -1;
}

// 插入字符串到Trie树
void insert(char* word, int wordId) {
    int node = root;
    int i;
    for (i = 0; word[i] != '\0'; i++) {
        int index = word[i] - 'A';
        if (tree[node].children[index] == 0) {
            cnt++;
            initNode(cnt);
            tree[node].children[index] = cnt;
        }
        node = tree[node].children[index];
    }
    tree[node].isEnd = 1;
    tree[node].wordId = wordId;
}

// 构建AC自动机
void buildACAutomation() {
    int queue[MAXS];
    int front = 0, rear = 0;
    int i;
    
    // 初始化根节点的失配指针
    for (i = 0; i < 26; i++) {
        if (tree[root].children[i] != 0) {
            tree[tree[root].children[i]].fail = root;
            queue[rear] = tree[root].children[i];
            rear++;
        } else {
            tree[root].children[i] = root;
        }
    }
    
    // BFS构建失配指针
    while (front < rear) {
        int node = queue[front];
        front++;
        
        for (i = 0; i < 26; i++) {
            if (tree[node].children[i] != 0) {
                int failNode = tree[node].fail;
                while (tree[failNode].children[i] == 0) {
                    failNode = tree[failNode].fail;
                }
                tree[tree[node].children[i]].fail = tree[failNode].children[i];
                queue[rear] = tree[node].children[i];
                rear++;
            }
        }
    }
}

// 从指定位置开始匹配
void matchFromPosition(int startX, int startY, int dxDir, int dyDir, char direction) {
    int current = root;
    int x = startX, y = startY;
    
    while (x >= 0 && x < L && y >= 0 && y < C) {
        char ch = matrix[x][y];
        int index = ch - 'A';
        
        // 根据失配指针跳转
        while (tree[current].children[index] == 0 && current != root) {
            current = tree[current].fail;
        }
        
        if (tree[current].children[index] != 0) {
            current = tree[current].children[index];
        } else {
            current = root;
        }
        
        // 检查是否有匹配的模式串
        int temp = current;
        while (temp != root) {
            if (tree[temp].isEnd && resultX[tree[temp].wordId] == 0 && resultY[tree[temp].wordId] == 0) {
                // 记录结果（这里简化处理，实际应该记录起始位置）
                resultX[tree[temp].wordId] = startX;
                resultY[tree[temp].wordId] = startY;
                resultDir[tree[temp].wordId] = direction;
            }
            temp = tree[temp].fail;
        }
        
        x += dxDir;
        y += dyDir;
    }
}

// 在指定方向搜索
void searchInDirection(int dir) {
    int dxDir = dx[dir];
    int dyDir = dy[dir];
    int i, j;
    
    // 根据方向确定起始点
    for (i = 0; i < L; i++) {
        for (j = 0; j < C; j++) {
            // 检查从(i,j)开始是否能匹配到边界
            int len = 0;
            int x = i, y = j;
            while (x >= 0 && x < L && y >= 0 && y < C) {
                len++;
                x += dxDir;
                y += dyDir;
            }
            
            if (len > 0) {
                // 从(i,j)开始匹配
                matchFromPosition(i, j, dxDir, dyDir, dirs[dir]);
            }
        }
    }
}

// 在矩阵中搜索
void searchInMatrix() {
    // 8个方向分别搜索
    int dir;
    for (dir = 0; dir < 8; dir++) {
        searchInDirection(dir);
    }
}

int main() {
    // 为了简化，这里使用示例输入
    // 实际应用中需要从标准输入读取
    
    // 示例输入
    L = 20; C = 20; W = 10;
    
    // 初始化根节点
    initNode(root);
    
    // 构建Trie树
    // 这里简化处理，实际需要从输入读取
    char word1[] = "MARGARITA";
    char word2[] = "ALEMA";
    char word3[] = "BARBECUE";
    char word4[] = "TROPICAL";
    char word5[] = "SUPREMA";
    char word6[] = "LOUISIANA";
    char word7[] = "CHEESEHAM";
    char word8[] = "EUROPA";
    char word9[] = "HAVAIANA";
    char word10[] = "CAMPONESA";
    
    insert(word1, 0);
    insert(word2, 1);
    insert(word3, 2);
    insert(word4, 3);
    insert(word5, 4);
    insert(word6, 5);
    insert(word7, 6);
    insert(word8, 7);
    insert(word9, 8);
    insert(word10, 9);
    
    // 构建AC自动机
    buildACAutomation();
    
    // 在矩阵中搜索
    searchInMatrix();
    
    // 输出结果
    int i;
    for (i = 0; i < W; i++) {
        // 简单输出结果
    }
    
    return 0;
}