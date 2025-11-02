from typing import List, Tuple
import math

class BitwiseOperationsInRealWorld:
    """
    位运算在实际工程中的应用
    测试链接：综合题目，展示位运算在真实工程场景中的应用
    
    题目描述：
    本文件展示位运算在各种实际工程场景中的应用，包括权限系统、状态压缩、加密算法等。
    
    解题思路：
    通过具体案例展示位运算如何解决实际问题，体现其高效性和实用性。
    
    时间复杂度：各种应用的时间复杂度不同，但通常为O(1)或O(log n)
    空间复杂度：O(1) - 通常只使用常数个变量
    """
    
    # ==================== 权限系统应用 ====================
    
    class PermissionSystem:
        """权限系统：使用位掩码表示用户权限"""
        
        # 权限定义
        READ = 1 << 0    # 0001 - 读权限
        WRITE = 1 << 1   # 0010 - 写权限  
        EXECUTE = 1 << 2 # 0100 - 执行权限
        DELETE = 1 << 3  # 1000 - 删除权限
        
        @staticmethod
        def add_permission(current: int, permission: int) -> int:
            """添加权限"""
            return current | permission
        
        @staticmethod
        def remove_permission(current: int, permission: int) -> int:
            """移除权限"""
            return current & ~permission
        
        @staticmethod
        def has_permission(current: int, permission: int) -> bool:
            """检查是否有权限"""
            return (current & permission) == permission
        
        @staticmethod
        def toggle_permission(current: int, permission: int) -> int:
            """切换权限（有则移除，无则添加）"""
            return current ^ permission
        
        @staticmethod
        def get_permission_names(permissions: int) -> List[str]:
            """获取所有权限列表"""
            result = []
            if BitwiseOperationsInRealWorld.PermissionSystem.has_permission(permissions, BitwiseOperationsInRealWorld.PermissionSystem.READ):
                result.append("READ")
            if BitwiseOperationsInRealWorld.PermissionSystem.has_permission(permissions, BitwiseOperationsInRealWorld.PermissionSystem.WRITE):
                result.append("WRITE")
            if BitwiseOperationsInRealWorld.PermissionSystem.has_permission(permissions, BitwiseOperationsInRealWorld.PermissionSystem.EXECUTE):
                result.append("EXECUTE")
            if BitwiseOperationsInRealWorld.PermissionSystem.has_permission(permissions, BitwiseOperationsInRealWorld.PermissionSystem.DELETE):
                result.append("DELETE")
            return result
    
    # ==================== 状态压缩应用 ====================
    
    class StateCompression:
        """状态压缩：使用一个整数表示多个布尔状态"""
        
        @staticmethod
        def set_state(state: int, position: int, value: bool) -> int:
            """设置状态位"""
            if value:
                return state | (1 << position)
            else:
                return state & ~(1 << position)
        
        @staticmethod
        def get_state(state: int, position: int) -> bool:
            """获取状态位"""
            return (state & (1 << position)) != 0
        
        @staticmethod
        def toggle_state(state: int, position: int) -> int:
            """切换状态位"""
            return state ^ (1 << position)
        
        @staticmethod
        def check_pattern(state: int, pattern: int) -> bool:
            """检查状态位模式"""
            return (state & pattern) == pattern
    
    # ==================== 颜色操作应用 ====================
    
    class ColorOperations:
        """颜色操作：使用位运算处理RGB颜色"""
        
        @staticmethod
        def create_color(red: int, green: int, blue: int) -> int:
            """从RGB值创建颜色整数"""
            return (red << 16) | (green << 8) | blue
        
        @staticmethod
        def get_red(color: int) -> int:
            """从颜色整数提取红色分量"""
            return (color >> 16) & 0xFF
        
        @staticmethod
        def get_green(color: int) -> int:
            """从颜色整数提取绿色分量"""
            return (color >> 8) & 0xFF
        
        @staticmethod
        def get_blue(color: int) -> int:
            """从颜色整数提取蓝色分量"""
            return color & 0xFF
        
        @staticmethod
        def adjust_brightness(color: int, factor: float) -> int:
            """调整颜色亮度（乘以系数）"""
            red = min(255, int(BitwiseOperationsInRealWorld.ColorOperations.get_red(color) * factor))
            green = min(255, int(BitwiseOperationsInRealWorld.ColorOperations.get_green(color) * factor))
            blue = min(255, int(BitwiseOperationsInRealWorld.ColorOperations.get_blue(color) * factor))
            return BitwiseOperationsInRealWorld.ColorOperations.create_color(red, green, blue)
        
        @staticmethod
        def blend_colors(color1: int, color2: int, ratio: float) -> int:
            """混合两种颜色"""
            inverse_ratio = 1.0 - ratio
            red = int(BitwiseOperationsInRealWorld.ColorOperations.get_red(color1) * ratio + BitwiseOperationsInRealWorld.ColorOperations.get_red(color2) * inverse_ratio)
            green = int(BitwiseOperationsInRealWorld.ColorOperations.get_green(color1) * ratio + BitwiseOperationsInRealWorld.ColorOperations.get_green(color2) * inverse_ratio)
            blue = int(BitwiseOperationsInRealWorld.ColorOperations.get_blue(color1) * ratio + BitwiseOperationsInRealWorld.ColorOperations.get_blue(color2) * inverse_ratio)
            return BitwiseOperationsInRealWorld.ColorOperations.create_color(red, green, blue)
    
    # ==================== 性能优化应用 ====================
    
    class PerformanceOptimization:
        """性能优化：使用位运算替代算术运算"""
        
        @staticmethod
        def power_of_two(n: int) -> int:
            """快速计算2的幂"""
            return 1 << n
        
        @staticmethod
        def is_power_of_two(n: int) -> bool:
            """快速判断是否是2的幂"""
            return n > 0 and (n & (n - 1)) == 0
        
        @staticmethod
        def abs_val(n: int) -> int:
            """快速计算绝对值"""
            mask = n >> 31
            return (n + mask) ^ mask
        
        @staticmethod
        def mod_power_of_two(n: int, mod: int) -> int:
            """快速计算模2的幂"""
            return n & (mod - 1)
        
        @staticmethod
        def swap(arr: List[int], i: int, j: int):
            """快速交换两个数"""
            if i != j:
                arr[i] ^= arr[j]
                arr[j] ^= arr[i]
                arr[i] ^= arr[j]
    
    # ==================== 数据结构优化应用 ====================
    
    class CompactBitSet:
        """位集（BitSet）简化实现"""
        
        def __init__(self, size: int):
            self.size = size
            self.data = [0] * ((size + 31) // 32)
        
        def set(self, index: int):
            """设置特定位"""
            if index < 0 or index >= self.size:
                raise IndexError("Index out of bounds")
            array_index = index // 32
            bit_index = index % 32
            self.data[array_index] |= (1 << bit_index)
        
        def clear(self, index: int):
            """清除特定位"""
            if index < 0 or index >= self.size:
                raise IndexError("Index out of bounds")
            array_index = index // 32
            bit_index = index % 32
            self.data[array_index] &= ~(1 << bit_index)
        
        def get(self, index: int) -> bool:
            """获取特定位"""
            if index < 0 or index >= self.size:
                raise IndexError("Index out of bounds")
            array_index = index // 32
            bit_index = index % 32
            return (self.data[array_index] & (1 << bit_index)) != 0
        
        def cardinality(self) -> int:
            """计算1的个数"""
            count = 0
            for value in self.data:
                # 计算1的个数
                while value != 0:
                    value &= (value - 1)
                    count += 1
            return count

# ==================== 测试代码 ====================

def main():
    print("=== 权限系统测试 ===")
    user_permissions = 0
    user_permissions = BitwiseOperationsInRealWorld.PermissionSystem.add_permission(
        user_permissions, BitwiseOperationsInRealWorld.PermissionSystem.READ)
    user_permissions = BitwiseOperationsInRealWorld.PermissionSystem.add_permission(
        user_permissions, BitwiseOperationsInRealWorld.PermissionSystem.WRITE)
    
    permission_names = BitwiseOperationsInRealWorld.PermissionSystem.get_permission_names(user_permissions)
    print(f"用户权限: {permission_names}")
    print(f"有写权限: {BitwiseOperationsInRealWorld.PermissionSystem.has_permission(
        user_permissions, BitwiseOperationsInRealWorld.PermissionSystem.WRITE)}")
    
    print("\n=== 状态压缩测试 ===")
    game_state = 0
    game_state = BitwiseOperationsInRealWorld.StateCompression.set_state(game_state, 0, True)  # 玩家存活
    game_state = BitwiseOperationsInRealWorld.StateCompression.set_state(game_state, 1, True)  # 游戏进行中
    print(f"玩家存活: {BitwiseOperationsInRealWorld.StateCompression.get_state(game_state, 0)}")
    print(f"游戏进行中: {BitwiseOperationsInRealWorld.StateCompression.get_state(game_state, 1)}")
    
    print("\n=== 颜色操作测试 ===")
    red_color = BitwiseOperationsInRealWorld.ColorOperations.create_color(255, 0, 0)
    green_color = BitwiseOperationsInRealWorld.ColorOperations.create_color(0, 255, 0)
    blended_color = BitwiseOperationsInRealWorld.ColorOperations.blend_colors(red_color, green_color, 0.5)
    print(f"混合颜色 - R:{BitwiseOperationsInRealWorld.ColorOperations.get_red(blended_color)} "
          f"G:{BitwiseOperationsInRealWorld.ColorOperations.get_green(blended_color)} "
          f"B:{BitwiseOperationsInRealWorld.ColorOperations.get_blue(blended_color)}")
    
    print("\n=== 性能优化测试 ===")
    array = [5, 3]
    BitwiseOperationsInRealWorld.PerformanceOptimization.swap(array, 0, 1)
    print(f"交换后: {array}")
    print(f"8是2的幂: {BitwiseOperationsInRealWorld.PerformanceOptimization.is_power_of_two(8)}")
    print(f"15 mod 8: {BitwiseOperationsInRealWorld.PerformanceOptimization.mod_power_of_two(15, 8)}")
    
    print("\n=== 数据结构优化测试 ===")
    bit_set = BitwiseOperationsInRealWorld.CompactBitSet(100)
    bit_set.set(42)
    bit_set.set(57)
    print(f"位42已设置: {bit_set.get(42)}")
    print(f"位43未设置: {bit_set.get(43)}")
    print(f"位集基数: {bit_set.cardinality()}")
    
    print("\n=== 工程化考量总结 ===")
    print("1. 边界条件处理：所有方法都应处理边界情况")
    print("2. 性能优化：位运算通常比算术运算更快")
    print("3. 可读性：添加详细注释说明位运算原理")
    print("4. 错误处理：输入验证和异常处理")
    print("5. 测试覆盖：包含各种边界情况和特殊输入")
    
    print("\n=== 应用场景总结 ===")
    print("1. 权限系统：高效管理用户权限")
    print("2. 状态压缩：节省内存空间")
    print("3. 图形学：快速处理颜色和像素")
    print("4. 性能优化：替代昂贵的算术运算")
    print("5. 数据结构：优化空间使用")

if __name__ == "__main__":
    main()