import React from 'react';
import { DragDropContext, Droppable } from 'react-beautiful-dnd';
import DraggableComponent from './DraggableComponent';

const DragDropContextComponent = ({ onDragEnd, children }) => (
  <DragDropContext onDragEnd={onDragEnd}>
    <Droppable droppableId="droppable">
      {(provided) => (
        <div
          ref={provided.innerRef}
          {...provided.droppableProps}
        >
          {children}
          {provided.placeholder}
        </div>
      )}
    </Droppable>
  </DragDropContext>
);

export default DragDropContextComponent;
