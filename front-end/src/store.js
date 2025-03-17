import { configureStore, createSlice } from "@reduxjs/toolkit";

let modalSwitch = createSlice({
    name : 'modalSwitch',
    initialState : false,
    reducers : {
        setModalSwitch(state){
            if(state == false){
                return true;
            }else{
                return false;
            }
            
        }
    }
});

let likeSwitch = createSlice({
    name : 'likeSwitch',
    initialState : false,
    reducers : {
        setLikeSwitch(state){
            if(state == false){
                return true;
            }else{
                return false;
            }
            
        }
    }
})

let postList = createSlice({
    name : 'postList',
    initialState : "초기값",
    reducers : {
        setPostList(state, actions){
            console.log("state값 : ", state );
            console.log("액션스값 : ", actions);
            return actions;
        }
    }

})






// export
export let { setModalSwitch } = modalSwitch.actions
export let { setLikeSwitch } = likeSwitch.actions
export let { setPostList } = postList.actions

export default configureStore({
    reducer: { 
        modalSwitch : modalSwitch.reducer,
        likeSwitch : likeSwitch.reducer,
        postList : postList.reducer
    }
})