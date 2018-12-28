import { handleActions } from 'redux-actions'
import { STATUS } from '../types/register'
import { USER } from '../types/register'
import { PLACEID } from '../types/register'
import { DEVICE } from '../types/register'
import { NUMBERS } from '../types/register'
import { PLACEDETAILID } from '../types/register'
import { MESSAGEID } from '../types/register'
    const defaultState = {
        code:1,
        user:{},
        place_id:'',
        place_detail_id:'',
        device_detailed:{},
        message_id:{},
        page_count:20,
        page_number:1,
        user_id:500,
        flag:[
            {
                label:'新增',
                value:1
            },
            {
                label:'正常',
                value:9
            },
            {
                label:'欠费',
                value:10
            },
            {
                label:'断线',
                value:20
            },
            {
                label:'删除',
                value:99
            }
        ],
        message_flag:[
            {
                label:'触发报警',
                value:1,
                type:'danger'
            },
            {
                label:'确认信息',
                value:10,
                type:'primary'
            },
            {
                label:'已删除',
                value:99,
                type:''
            }
        ],
        
    }
    
    export default handleActions({
        [STATUS]( state , action ){
            return {
              ...state,
              code:action.payload
            }
        },
        [USER]( state , action ){
            return {
              ...state,
              user:action.payload
            }
        },
        [PLACEID]( state , action ){
            return {
              ...state,
              place_id:action.payload
            }
        },
        [DEVICE]( state , action ){
            return {
              ...state,
              device_detailed:action.payload
            }
        },
        [NUMBERS]( state , action ){
            return {
              ...state,
              page_number:action.payload
            }
        },
        [PLACEDETAILID]( state , action ){
            return {
              ...state,
              place_detail_id:action.payload
            }
        },
        [MESSAGEID]( state , action ){
            return {
              ...state,
              message_id:action.payload
            }
        },
    },defaultState)
