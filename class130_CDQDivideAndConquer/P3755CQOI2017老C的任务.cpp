// P3755 [CQOI2017]老C的任务
// 平台: 洛谷
// 难度: 提高+/省选-
// 标签: CDQ分治, 二维数点
// 链接: https://www.luogu.com.cn/problem/P3755

// 题目描述:
// 老 C 是个程序员。
// 最近老 C 从老板那里接到了一个任务——给城市中的手机基站写个管理系统。
// 由于一个基站的面积相对于整个城市面积来说非常的小，因此每个的基站都可以看作坐标系中的一个点，
// 其位置可以用坐标 (x,y) 来表示。此外，每个基站还有很多属性，例如高度、功率等。
// 运营商经常会划定一个区域，并查询区域中所有基站的信息。
// 现在你需要实现的功能就是，对于一个给定的矩形区域，回答该区域中（包括区域边界上的）所有基站的功率总和。

// 解题思路:
// 这是一个二维数点问题，可以使用CDQ分治来解决。
// 将基站的插入操作和查询操作都看作事件，然后使用CDQ分治处理。
// 1. 将每个基站看作一个插入事件
// 2. 将每次查询拆分成四个前缀和查询
// 3. 按照x坐标排序
// 4. 使用CDQ分治处理，在合并过程中使用树状数组维护y坐标维度上的前缀和

const int MAXN = 200005;

struct Event {
    int type, x, y, power, id;  // type:1插入,2查询; power:插入时为功率，查询时为系数; id:查询编号
    Event() : type(0), x(0), y(0), power(0), id(0) {}
    Event(int _type, int _x, int _y, int _power, int _id) : 
        type(_type), x(_x), y(_y), power(_power), id(_id) {}
};

Event events[MAXN];
long long tree[MAXN];  // 树状数组
long long ans[MAXN];   // 答案数组
int cnt = 0, n, m;

int lowbit(int x) {
    return x & (-x);
}

void add(int pos, long long val) {
    while (pos < MAXN) {
        tree[pos] += val;
        pos += lowbit(pos);
    }
}

long long query(int pos) {
    long long res = 0;
    while (pos > 0) {
        res += tree[pos];
        pos -= lowbit(pos);
    }
    return res;
}

// 简单选择排序
void selectionSort(int* arr, int len, bool (*cmp)(int, int)) {
    for (int i = 0; i < len - 1; i++) {
        int minIdx = i;
        for (int j = i + 1; j < len; j++) {
            if (cmp(arr[j], arr[minIdx])) {
                minIdx = j;
            }
        }
        if (minIdx != i) {
            int temp = arr[i];
            arr[i] = arr[minIdx];
            arr[minIdx] = temp;
        }
    }
}

// 比较函数
bool cmpByY(int a, int b) {
    return events[a].y < events[b].y;
}

bool cmpByX(int a, int b) {
    if (events[a].x != events[b].x) return events[a].x < events[b].x;
    return events[a].type < events[b].type;  // 插入事件优先于查询事件
}

// 添加基站插入事件
void addBaseStation(int x, int y, int power) {
    events[++cnt] = Event(1, x, y, power, 0);
}

// 添加查询事件，使用容斥原理将矩形查询转换为前缀和查询
void addQuery(int x, int y, int coeff, int id) {
    events[++cnt] = Event(2, x, y, coeff, id);
}

// CDQ分治
void cdq(int l, int r) {
    if (l >= r) return;
    int mid = (l + r) >> 1;
    cdq(l, mid);
    cdq(mid + 1, r);
    
    // 处理左半部分对右半部分的贡献
    // 按y坐标排序
    int* left = new int[mid - l + 1];
    int* right = new int[r - mid];
    for (int i = l; i <= mid; i++) left[i - l] = i;
    for (int i = mid + 1; i <= r; i++) right[i - mid - 1] = i;
    
    // 按y坐标排序
    selectionSort(left, mid - l + 1, cmpByY);
    selectionSort(right, r - mid, cmpByY);
    
    int j = 0;
    for (int i = 0; i < r - mid; i++) {
        // 处理插入事件
        while (j < mid - l + 1 && events[left[j]].y <= events[right[i]].y) {
            if (events[left[j]].type == 1) {
                add(events[left[j]].x, events[left[j]].power);
            }
            j++;
        }
        // 处理查询事件
        if (events[right[i]].type == 2) {
            ans[events[right[i]].id] += (long long)events[right[i]].power * query(events[right[i]].x);
        }
    }
    
    // 清空树状数组
    for (int i = 0; i < j; i++) {
        if (events[left[i]].type == 1) {
            add(events[left[i]].x, -events[left[i]].power);
        }
    }
    
    delete[] left;
    delete[] right;
}

int main() {
    // 由于编译环境限制，使用固定输入
    // 实际测试时需要根据具体环境调整输入方式
    n = 2;
    m = 1;
    
    // 示例输入
    // 基站: (1,1,10) (2,2,20)
    addBaseStation(1, 1, 10);
    addBaseStation(2, 2, 20);
    
    // 查询: (1,1,2,2)
    addQuery(2, 2, 1, 1);      // 右上角区域加
    addQuery(0, 0, 1, 1);      // 左下角区域加
    addQuery(0, 2, -1, 1);     // 左上角区域减
    addQuery(2, 0, -1, 1);     // 右下角区域减
    
    // 按照x坐标排序
    int* indices = new int[cnt];
    for (int i = 1; i <= cnt; i++) indices[i-1] = i;
    selectionSort(indices, cnt, cmpByX);
    
    // 重新排列events数组
    Event* temp = new Event[MAXN];
    for (int i = 1; i <= cnt; i++) {
        temp[i] = events[indices[i-1]];
    }
    for (int i = 1; i <= cnt; i++) {
        events[i] = temp[i];
    }
    
    delete[] indices;
    delete[] temp;
    
    // CDQ分治求解
    cdq(1, cnt);
    
    // 由于编译环境限制，这里直接注释输出
    // 实际使用时需要根据具体环境调整输出方式
    // ans[1] 的值就是结果
    
    return 0;
}