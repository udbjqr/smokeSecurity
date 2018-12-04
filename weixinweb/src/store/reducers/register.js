import { handleActions } from 'redux-actions'
import { STATUS } from '../types/register'
import { USER } from '../types/register'
import { PLACEID } from '../types/register'
import { DEVICE } from '../types/register'
    const defaultState = {
        code:1,
        user:{},
        place_id:'',
        device_detailed:{},
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
        ]
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
    },defaultState)
