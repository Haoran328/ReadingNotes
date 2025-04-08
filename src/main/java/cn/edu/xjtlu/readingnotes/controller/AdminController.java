@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepo userRepo;

    @PutMapping("/user/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setActive(false);
        return ResponseEntity.ok(userRepo.save(user));
    }

    // 用户管理接口
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @PutMapping("/user/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserStatus(
        @PathVariable Long id, 
        @RequestParam boolean active) {
        User user = userRepo.findById(id).orElseThrow();
        user.setActive(active);
        return ResponseEntity.ok(userRepo.save(user));
    }

    // 添加用户删除接口
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
    }
}