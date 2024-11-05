import ProjectCurrentStateElement from "./ProjectCurrentStateElement";

interface CurrentStateProps {
  blueStateSubject: string;
  blueStateNum: number;
  pinkStateSubject: string;
  pinkStateNum: number;
  yellowStateSubject: string;
  yellowStateNum: number;
}

function ProjectCurrentState({
  blueStateSubject,
  blueStateNum,
  pinkStateSubject,
  pinkStateNum,
  yellowStateSubject,
  yellowStateNum,
}: CurrentStateProps) {
  return (
    <>
      <div className="flex gap-4">
        <ProjectCurrentStateElement
          colorCode={"#1B77F2"}
          statusSubject={blueStateSubject}
          statusNum={blueStateNum}
        />
        <ProjectCurrentStateElement
          colorCode={"#F25C84"}
          statusSubject={pinkStateSubject}
          statusNum={pinkStateNum}
        />
        <ProjectCurrentStateElement
          colorCode={"#FFF06C"}
          statusSubject={yellowStateSubject}
          statusNum={yellowStateNum}
        />
      </div>
    </>
  );
}
export default ProjectCurrentState;
