package com.gongcha.berrymatch.group;


import com.gongcha.berrymatch.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class GroupController {


    @Autowired
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }


    @PostMapping("/creategroup")
    public ApiResponse<GroupResponse> createGroup(@RequestBody GroupRequest groupRequest) {
        try {
            GroupResponse groupResponse = groupService.createGroup(groupRequest);
            return ApiResponse.ok(groupResponse);
        } catch (IllegalStateException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }


    @PostMapping("/joingroup")
    public ApiResponse<GroupResponse> joinGroup(@RequestBody GroupRequest groupRequest) {
        try {
            GroupResponse groupResponse = groupService.joinGroup(groupRequest);
            return ApiResponse.ok(groupResponse);
        } catch (IllegalStateException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/{id}/groupleave")
    public ApiResponse<GroupResponse> leaveGroup(@PathVariable Long id ,@RequestBody GroupRequest groupRequest){
        GroupResponse groupResponse = groupService.leaveGroup(groupRequest);
        return ApiResponse.ok(groupResponse);
    }




    @GetMapping("/group/user/{id}")
    public ApiResponse<GroupResponse> getGroupState(@PathVariable Long id) {
        System.out.println("유저그룹아이디 들어옴"+id);
         GroupResponse groupResponse  = groupService.getGroupByUserId(id);;
        return ApiResponse.ok(groupResponse);
    }



    @GetMapping("/group/user/{id}/live")
    public ApiResponse<GroupResponse> getLiveGroupState(@PathVariable Long id) {
        System.out.println("유저그룹아이디 들어옴"+id);
        GroupResponse groupResponse  = groupService.getGroupByUserId(id);;
        return ApiResponse.ok(groupResponse);
    }

}
