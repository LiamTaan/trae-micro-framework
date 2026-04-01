-- 初始化默认租户
INSERT INTO sys_tenant (tenant_name, tenant_code, status, description) VALUES 
('默认租户', 'default', 1, '系统默认租户');

-- 初始化管理员用户（密码：123456，已加密）
INSERT INTO sys_user (tenant_id, username, password, nickname, email, phone, avatar, gender, status, create_by, update_by) VALUES 
(1, 'admin', '$2a$10$a/3hYQv5FZs3SwSMRJPuW.IUBgcWNJwl17P4SzXn8b.Mpf2xpvixS', '管理员', 'admin@trae-ai.com', '13800138000', '', 1, 1, 1, 1);

-- 初始化基础角色
INSERT INTO sys_role (tenant_id, name, code, description, status, create_by, update_by) VALUES 
(1, '超级管理员', 'admin', '拥有系统所有权限', 1, 1, 1),
(1, '普通用户', 'user', '拥有基础操作权限', 1, 1, 1);

-- 初始化用户角色关联
INSERT INTO sys_user_role (user_id, role_id, tenant_id, create_by, update_by) VALUES 
(1, 1, 1, 1, 1);

-- 初始化基础菜单
INSERT INTO sys_menu (parent_id, tenant_id, name, type, path, component, perms, icon, sort, status, visible, create_by, update_by) VALUES 
-- 目录
(0, 1, '系统管理', 0, '/system', 'Layout', '', 'system', 1, 1, 1, 1, 1),
(0, 1, '文件管理', 0, '/file', 'Layout', '', 'file', 2, 1, 1, 1, 1),

-- 系统管理子菜单
(1, 1, '用户管理', 1, 'user', 'system/user/index', 'system:user:list', '', 1, 1, 1, 1, 1),
(1, 1, '角色管理', 1, 'role', 'system/role/index', 'system:role:list', '', 2, 1, 1, 1, 1),
(1, 1, '菜单管理', 1, 'menu', 'system/menu/index', 'system:menu:list', '', 3, 1, 1, 1, 1),

-- 用户管理按钮 (parent_id = 3, 对应用户管理ID)
(3, 1, '用户查询', 2, '', '', 'system:user:query', '', 1, 1, 1, 1, 1),
(3, 1, '用户新增', 2, '', '', 'system:user:add', '', 2, 1, 1, 1, 1),
(3, 1, '用户修改', 2, '', '', 'system:user:update', '', 3, 1, 1, 1, 1),
(3, 1, '用户删除', 2, '', '', 'system:user:delete', '', 4, 1, 1, 1, 1),

-- 角色管理按钮 (parent_id = 4, 对应角色管理ID)
(4, 1, '角色查询', 2, '', '', 'system:role:query', '', 1, 1, 1, 1, 1),
(4, 1, '角色新增', 2, '', '', 'system:role:add', '', 2, 1, 1, 1, 1),
(4, 1, '角色修改', 2, '', '', 'system:role:update', '', 3, 1, 1, 1, 1),
(4, 1, '角色删除', 2, '', '', 'system:role:delete', '', 4, 1, 1, 1, 1),

-- 菜单管理按钮 (parent_id = 5, 对应菜单管理ID)
(5, 1, '菜单查询', 2, '', '', 'system:menu:query', '', 1, 1, 1, 1, 1),
(5, 1, '菜单新增', 2, '', '', 'system:menu:add', '', 2, 1, 1, 1, 1),
(5, 1, '菜单修改', 2, '', '', 'system:menu:update', '', 3, 1, 1, 1, 1),
(5, 1, '菜单删除', 2, '', '', 'system:menu:delete', '', 4, 1, 1, 1, 1),

-- 文件管理子菜单
(2, 1, '文件上传', 1, 'file-upload', 'file/upload/index', 'file:upload', '', 1, 1, 1, 1, 1),
(2, 1, '文件列表', 1, 'file-list', 'file/list/index', 'file:list', '', 2, 1, 1, 1, 1),

-- 文件管理按钮 (parent_id = 19, 对应文件列表ID)
(19, 1, '文件下载', 2, '', '', 'file:download', '', 1, 1, 1, 1, 1),
(19, 1, '文件删除', 2, '', '', 'file:delete', '', 2, 1, 1, 1, 1);

-- 初始化角色菜单关联
INSERT INTO sys_role_menu (role_id, menu_id, tenant_id, create_by, update_by) VALUES 
-- 超级管理员拥有所有菜单权限
(1, 1, 1, 1, 1), (1, 2, 1, 1, 1), 
(1, 3, 1, 1, 1), (1, 4, 1, 1, 1), (1, 5, 1, 1, 1), 
(1, 6, 1, 1, 1), (1, 7, 1, 1, 1), (1, 8, 1, 1, 1), (1, 9, 1, 1, 1), 
(1, 10, 1, 1, 1), (1, 11, 1, 1, 1), (1, 12, 1, 1, 1), (1, 13, 1, 1, 1), 
(1, 14, 1, 1, 1), (1, 15, 1, 1, 1), (1, 16, 1, 1, 1), (1, 17, 1, 1, 1), 
(1, 18, 1, 1, 1), (1, 19, 1, 1, 1), (1, 20, 1, 1, 1), (1, 21, 1, 1, 1),

-- 普通用户拥有部分菜单权限
(2, 2, 1, 1, 1), (2, 18, 1, 1, 1), (2, 19, 1, 1, 1), (2, 20, 1, 1, 1), (2, 21, 1, 1, 1);