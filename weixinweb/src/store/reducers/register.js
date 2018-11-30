import { handleActions } from 'redux-actions'
    import { STATUS } from '../types/register'
    
    const defaultState = {code:1}
    
    export default handleActions({
        [STATUS]( state , action ){
            return {
              ...state,
              code:action.payload
            }
        },
    },defaultState)
