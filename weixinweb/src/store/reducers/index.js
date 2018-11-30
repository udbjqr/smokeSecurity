import { combineReducers } from 'redux'
import counter from './counter'
import register from './register'

export default combineReducers({
  counter,
  register
})