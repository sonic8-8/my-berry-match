import React, { useState, useRef, useEffect } from 'react';
import styles from './BackgroundMusic.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay, faPause } from '@fortawesome/free-solid-svg-icons';

const BackgroundMusic = () => {
  const audioRef = useRef(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTrack, setCurrentTrack] = useState(0);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const tracks = [
    {
      title: 'Track 1',
      src: 'https://thank-you-berrymatch-bucket-0.s3.ap-northeast-2.amazonaws.com/music/MainBgm1.mp3',
    },
    {
      title: 'Track 2',
      src: 'https://thank-you-berrymatch-bucket-0.s3.ap-northeast-2.amazonaws.com/music/MainBgm2.mp3',
    },
    {
      title: 'Track 3',
      src: 'https://thank-you-berrymatch-bucket-0.s3.ap-northeast-2.amazonaws.com/music/MainBgm3.mp3',
    }
  ];

  const handleEnded = () => {
    setCurrentTrack((prevTrack) => (prevTrack + 1) % tracks.length);
    setIsPlaying(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };

  useEffect(() => {
    const audioElement = audioRef.current;

    if (audioElement) {
      audioElement.addEventListener('ended', handleEnded);

      return () => {
        audioElement.removeEventListener('ended', handleEnded);
      };
    }
  }, [audioRef.current]);

  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.load();
      if (isPlaying) {
        audioRef.current.play().catch(error => {
          console.error("음악 재생 중 오류:", error);
        });
      }
    }
  }, [currentTrack]);

  const togglePlayPause = () => {
    const audioElement = audioRef.current;
    if (audioElement) {
      if (isPlaying) {
        audioElement.pause();
      } else {
        audioElement.play().catch(error => {
          console.error("음악 재생 중 오류:", error);
        });
      }
      setIsPlaying(!isPlaying);
    }
  };

  const handleTrackChange = (index) => {
    setCurrentTrack(index);
    setIsPlaying(true);
  };

  return (
    <div className={styles.music_container}>
      <div className={styles.music_playing}>
        <h2>현재 재생 중: {tracks[currentTrack].title}</h2>
        <button onClick={togglePlayPause}>
          {isPlaying ? (
            <FontAwesomeIcon icon={faPause} />
          ) : (
            <FontAwesomeIcon icon={faPlay} />
          )}
        </button>
        <button onClick={handleOpenModal}>트랙 선택</button>
      </div>

      <audio ref={audioRef} key={tracks[currentTrack].src} loop>
        <source src={tracks[currentTrack].src} type="audio/mpeg" />
        Your browser does not support the audio element.
      </audio>

      {/* Modal */}
      {isModalOpen && (
        <div className={styles.modal} onClick={handleCloseModal}>
          <div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
            <h3>다른 트랙 선택:</h3>
            {tracks.map((track, index) => (
              <button key={index} onClick={() => handleTrackChange(index)}>
                {track.title}
              </button>
            ))}
            <button className={styles.closeButton} onClick={handleCloseModal}>닫기</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default BackgroundMusic;
